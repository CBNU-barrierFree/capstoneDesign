package com.example.demo.hotspot.importer;

import com.example.demo.hotspot.HotspotEntity;
import com.example.demo.hotspot.HotspotRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class HotspotImportService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HotspotRepository repo;

    public HotspotImportService(HotspotRepository repo) {
        this.repo = repo;
    }

    @Value("${traffic.api.base}")
    private String baseUrl;

    @Value("${traffic.api.key}")   // 반드시 인코딩된 키 (% 포함)
    private String serviceKeyEncoded;

    @Value("${traffic.api.rows:100}")
    private int rows;

    // =========================
    // 공개 API
    // =========================

    /** 1) 개별 구군 적재 */
    public int importYearArea(int year, int siDo, int guGun) {
        int totalSaved = 0;
        int pageNo = 1;

        while (true) {
            String body = fetchPage(year, siDo, guGun, rows, pageNo);
            if (body == null) break;

            try {
                JsonNode root = objectMapper.readTree(body);
                JsonNode bodyNode = root.path("response").path("body");
                int totalCount = bodyNode.path("totalCount").asInt(0);
                JsonNode itemsNode = bodyNode.path("items").path("item");

                if (itemsNode.isMissingNode() || itemsNode.isNull()) break;

                int pageSaved = 0;
                if (itemsNode.isArray()) {
                    for (JsonNode it : itemsNode) pageSaved += saveOne(it, year);
                } else {
                    pageSaved += saveOne(itemsNode, year);
                }
                totalSaved += pageSaved;

                int totalPages = (int) Math.ceil((double) totalCount / rows);
                if (pageNo >= totalPages) break;
                pageNo++;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("[IMPORT] FIN totalSaved=" + totalSaved);
        return totalSaved;
    }

    /** 2) 시도 전체 적재 */
    public int importYearForSiDo(int year, int siDo) {
        int grandTotal = 0;
        for (int guGun = 1; guGun <= 999; guGun++) {
            int saved = importYearArea(year, siDo, guGun);
            if (saved > 0) {
                grandTotal += saved;
            }
        }
        System.out.println("[IMPORT:SIDO] FIN siDo=" + siDo + " grandTotal=" + grandTotal);
        return grandTotal;
    }

    /** 3) 전국 전체 적재 */
    public int importYearAll(int year) {
        int national = 0;
        int[] SIDO_ALL = {11,26,27,28,29,30,31,36,41,42,43,44,45,46,47,48,50};
        for (int siDo : SIDO_ALL) {
            national += importYearForSiDo(year, siDo);
        }
        System.out.println("[IMPORT:ALL] FIN year=" + year + " nationalTotal=" + national);
        return national;
    }

    // =========================
    // 내부 유틸
    // =========================

    /** 페이지 요청 → JSON body */
    private String fetchPage(int year, Integer siDo, Integer guGun, int rows, int pageNo) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("[CONFIG] traffic.api.base 가 비어있습니다.");
        }
        if (serviceKeyEncoded == null || serviceKeyEncoded.isBlank() || !serviceKeyEncoded.contains("%")) {
            throw new IllegalStateException("[CONFIG] traffic.api.key 는 반드시 '인코딩된 키(%)' 여야 합니다.");
        }

        String common = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/getRestFrequentzoneBicycle")
                .queryParam("searchYearCd", year)
                .queryParam("siDo", siDo)
                .queryParam("guGun", guGun)
                .queryParam("type", "json")
                .queryParam("numOfRows", rows)
                .queryParam("pageNo", pageNo)
                .build(false) // 인코딩 하지 않음
                .toUriString();

        // 1) 소문자 serviceKey
        String url1 = common + "&serviceKey=" + serviceKeyEncoded;
        System.out.println("[IMPORT] url=" + url1);
        ResponseEntity<String> r1 = restTemplate.getForEntity(url1, String.class);
        System.out.println("[IMPORT] status=" + r1.getStatusCodeValue());
        if (r1.getBody() != null && !r1.getBody().startsWith("<")) {
            return r1.getBody();
        }
        logXmlErrorPreview(r1.getBody());

        // 2) 대문자 ServiceKey
        String url2 = common + "&ServiceKey=" + serviceKeyEncoded;
        System.out.println("[IMPORT] url(ServiceKey)=" + url2);
        ResponseEntity<String> r2 = restTemplate.getForEntity(url2, String.class);
        System.out.println("[IMPORT] status=" + r2.getStatusCodeValue());
        if (r2.getBody() != null && !r2.getBody().startsWith("<")) {
            return r2.getBody();
        }
        logXmlErrorPreview(r2.getBody());

        return null;
    }

    /** JSON 아이템 1건 → DB 저장 */
    private int saveOne(JsonNode it, int year) {
        String name = it.path("spot_nm").asText("-");
        double lat = it.path("la_crd").asDouble(Double.NaN);
        double lng = it.path("lo_crd").asDouble(Double.NaN);
        int accidents = it.path("occrrnc_cnt").asInt(0);
        int casualties = it.path("caslt_cnt").asInt(0);

        if (Double.isNaN(lat) || Double.isNaN(lng)) return 0;

        Optional<HotspotEntity> existing = repo.findFirstByStatYearAndLatAndLng(year, lat, lng);
        if (existing.isPresent()) {
            HotspotEntity cur = existing.get();
            cur.setName(name);
            cur.setAccidents(accidents);
            cur.setCasualties(casualties);
            repo.save(cur);
        } else {
            HotspotEntity e = new HotspotEntity();
            e.setName(name);
            e.setLat(lat);
            e.setLng(lng);
            e.setAccidents(accidents);
            e.setCasualties(casualties);
            e.setStatYear(year);
            repo.save(e);
        }
        return 1;
    }

    private void logXmlErrorPreview(String body) {
        if (body == null) return;
        System.out.println("[IMPORT] XML error (first 400):\n" + body.substring(0, Math.min(400, body.length())));
    }
}
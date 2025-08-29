package com.example.demo.hotspot.importer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class HotspotImportController {
    private final HotspotImportService service;
    public HotspotImportController(HotspotImportService service) { this.service = service; }

    // 1) 개별 구군 적재: /admin/import?year=2021&siDo=11&guGun=110
    @GetMapping("/import")
    public ResponseEntity<String> importOneGet(
            @RequestParam("year") Integer year,
            @RequestParam("siDo") Integer siDo,
            @RequestParam("guGun") Integer guGun
    ) {
        System.out.println("[ADMIN/GET] year=" + year + ", siDo=" + siDo + ", guGun=" + guGun);
        int saved = service.importYearArea(year, siDo, guGun);
        return ResponseEntity.ok("imported: " + saved);
    }

    // 2) 시도 전체 적재: /admin/import-sido?year=2021&siDo=11
    @GetMapping("/import-sido")
    public ResponseEntity<String> importSido(
            @RequestParam("year") Integer year,
            @RequestParam("siDo") Integer siDo
    ) {
        int saved = service.importYearForSiDo(year, siDo);
        return ResponseEntity.ok("sido imported: " + saved);
    }

    // 3) 전국 전체 적재: /admin/import-all?year=2021
    @GetMapping("/import-all")
    public ResponseEntity<String> importAll(@RequestParam("year") Integer year) {
        int saved = service.importYearAll(year);
        return ResponseEntity.ok("national imported: " + saved);
    }
}

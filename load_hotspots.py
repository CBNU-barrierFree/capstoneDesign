# -*- coding: utf-8 -*-
"""
B552061 frequentzoneBicycle → MySQL 적재 (전국 시도 자동 스캔 버전)
- serviceKey: 인코딩키 그대로(URL 인코딩된 값, %2F, %3D%3D 포함)
- BASE: 반드시 http:// 사용(https는 일부 환경에서 SSL/라우팅 오류)
- 구군 코드를 몰라도 '존재하는 구군만' 빠르게 스캔해서 적재
"""

import time
import requests
import pymysql

# ========= 1) 설정 =========
SERVICE_KEY = "RKqzhkFffQBQois3%2F3fNLq6CvB7O3BwmNxzFxoVJV8B%2FhgWdf2pXePTFxkNL420DzGOa42homOIiyKoOI3wWZw%3D%3D"
BASE = "http://apis.data.go.kr/B552061/frequentzoneBicycle/getRestFrequentzoneBicycle"
ROWS = 100

# 적재 연도들(예: 2021만 먼저 테스트 후 range로 확장)
YEARS = [2021]
# 전국 시도 코드(표준 17개)
SIDO_ALL = [11, 26, 27, 28, 29, 30, 31, 36, 41, 42, 43, 44, 45, 46, 47, 48, 50]

# MySQL 접속
DB_HOST = "127.0.0.1"
DB_PORT = 3306
DB_USER = "root"
DB_PASS = "Akdlfjqm11@"
DB_NAME = "api-data"

# ========= 2) DB 스키마/SQL =========
DDL = """
CREATE TABLE IF NOT EXISTS hotspots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    lat DOUBLE NOT NULL,
    lng DOUBLE NOT NULL,
    accidents INT NOT NULL,
    casualties INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_hotspots_year_lat_lng (year, lat, lng)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
"""

UPSERT_SQL = """
INSERT INTO hotspots (year, name, lat, lng, accidents, casualties)
VALUES (%s, %s, %s, %s, %s, %s)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  accidents = VALUES(accidents),
  casualties = VALUES(casualties),
  updated_at = CURRENT_TIMESTAMP;
"""

# ========= 3) API 유틸 =========
def build_url(encoded_key: str) -> str:
    # 인코딩된 서비스키는 URL에 직접, 나머지 파라미터는 params로(이중 인코딩 방지)
    return f"{BASE}?serviceKey={encoded_key}"

def fetch_page(year: int, siDo: int, guGun: int, page_no: int, rows: int = ROWS) -> dict:
    url = build_url(SERVICE_KEY)
    params = {
        "type": "json",
        "searchYearCd": year,
        "siDo": siDo,
        "guGun": guGun,
        "numOfRows": rows,
        "pageNo": page_no,
    }
    r = requests.get(url, params=params, timeout=30)
    if r.status_code != 200:
        raise RuntimeError(f"HTTP {r.status_code} / {r.text[:200]}")
    if r.text.lstrip().startswith("<"):
        # 공공데이터 오류시 XML로 옴 → 바로 예외
        raise RuntimeError("API returned XML error page:\n" + r.text[:400])
    return r.json()

def extract_items_and_count(data: dict):
    """
    응답 포맷 2종 지원:
      A) { "resultCode":"00", "items": { "item": [...] }, "totalCount":? }
      B) { "response": { "body": { "items": { "item": [...] }, "totalCount": N } } }
    반환: (items_list, totalCount_or_None)
    """
    # 포맷 A
    if "items" in data and isinstance(data["items"], dict):
        item = data["items"].get("item")
        items_list = [] if item is None else (item if isinstance(item, list) else [item])
        total_count = data.get("totalCount")
        try:
            total_count = int(total_count) if total_count is not None else None
        except Exception:
            total_count = None
        return items_list, total_count

    # 포맷 B
    resp = data.get("response", {})
    body = resp.get("body", {})
    item = body.get("items", {}).get("item")
    items_list = [] if item is None else (item if isinstance(item, list) else [item])
    total_count = body.get("totalCount")
    try:
        total_count = int(total_count) if total_count is not None else None
    except Exception:
        total_count = None
    return items_list, total_count

def iter_items(year: int, siDo: int, guGun: int, rows: int = ROWS):
    """
    totalCount가 없어도 동작:
    - 1페이지부터 호출, 받은 개수가 rows보다 작으면 마지막 페이지로 판단
    """
    page = 1
    saw_any = False
    while True:
        data = fetch_page(year, siDo, guGun, page_no=page, rows=rows)
        items, total_count = extract_items_and_count(data)

        if not items:
            if not saw_any:
                print(f"[SKIP] year={year} siDo={siDo} guGun={guGun} : no items")
            break

        if page == 1:
            if total_count is not None:
                approx_pages = (total_count + rows - 1) // rows
                print(f"[INFO] year={year} siDo={siDo} guGun={guGun} total={total_count} pages~={approx_pages}")
            else:
                print(f"[INFO] year={year} siDo={siDo} guGun={guGun} (totalCount 없음) 첫 페이지 {len(items)}건")

        saw_any = True
        for it in items:
            yield it

        if len(items) < rows:
            break  # 마지막 페이지
        page += 1
        time.sleep(0.15)  # 과호출 방지 살짝

# ========= 4) 매핑/DB =========
def map_item(it: dict, year: int):
    name = it.get("spot_nm", "-")
    try:
        lat = float(it.get("la_crd") or "nan")
        lng = float(it.get("lo_crd") or "nan")
    except Exception:
        return None
    try:
        accidents = int(it.get("occrrnc_cnt") or 0)
        casualties = int(it.get("caslt_cnt") or 0)
    except Exception:
        accidents, casualties = 0, 0
    if not (lat == lat and lng == lng):  # NaN 체크
        return None
    return (year, name, lat, lng, accidents, casualties)

def ensure_table(conn):
    with conn.cursor() as cur:
        cur.execute(DDL)
    conn.commit()

def upsert_batch(conn, rows_data):
    if not rows_data:
        return 0
    with conn.cursor() as cur:
        cur.executemany(UPSERT_SQL, rows_data)
    conn.commit()
    return len(rows_data)

def import_area(conn, year: int, siDo: int, guGun: int):
    batch, total_saved = [], 0
    for it in iter_items(year, siDo, guGun, rows=ROWS):
        row = map_item(it, year)
        if row is None:
            continue
        batch.append(row)
        if len(batch) >= 500:
            total_saved += upsert_batch(conn, batch)
            print(f"  [COMMIT] +{len(batch)} (total={total_saved})")
            batch = []
    if batch:
        total_saved += upsert_batch(conn, batch)
        print(f"  [COMMIT] +{len(batch)} (total={total_saved})")
    return total_saved

# ========= 5) 시도 전체 자동(구군 스캔) =========
def scan_valid_gugun(year: int, siDo: int, max_code: int = 400):
    """
    각 구군 코드를 1페이지(1건)만 조회해서 아이템이 있으면 유효로 간주.
    max_code는 보통 400 정도면 넉넉.
    """
    valid = []
    for guGun in range(1, max_code + 1):
        try:
            data = fetch_page(year, siDo, guGun, page_no=1, rows=1)
            items, _ = extract_items_and_count(data)
            if items:
                valid.append(guGun)
                print(f"[SCAN] siDo={siDo} guGun={guGun} OK")
        except Exception:
            pass
        time.sleep(0.03)
    return valid

def import_sido(conn, year: int, siDo: int):
    valid = scan_valid_gugun(year, siDo)
    total = 0
    for guGun in valid:
        print(f"\n[RUN:SIDO] year={year} siDo={siDo} guGun={guGun}")
        total += import_area(conn, year, siDo, guGun)
    print(f"[FIN:SIDO] year={year} siDo={siDo} total={total}")
    return total

# ========= 6) 메인 =========
def main():
    conn = pymysql.connect(
        host=DB_HOST, port=DB_PORT, user=DB_USER, password=DB_PASS,
        database=DB_NAME, autocommit=False, charset="utf8mb4"
    )
    try:
        ensure_table(conn)
        grand_total = 0
        for year in YEARS:
            for siDo in SIDO_ALL:
                print(f"\n===== [YEAR {year}] SIDO {siDo} 시작 =====")
                try:
                    saved = import_sido(conn, year, siDo)
                    grand_total += saved
                except Exception as e:
                    print(f"[ERR] year={year} siDo={siDo} -> {e}")
        print(f"\n[FIN] total inserted/updated rows = {grand_total}")
    finally:
        conn.close()

if __name__ == "__main__":
    main()
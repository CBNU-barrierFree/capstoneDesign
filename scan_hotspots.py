# -*- coding: utf-8 -*-
import sys, time, math, csv
import requests

# ----- 필수: 인코딩키 그대로! -----
SERVICE_KEY = "RKqzhkFffQBQois3%2F3fNLq6CvB7O3BwmNxzFxoVJV8B%2FhgWdf2pXePTFxkNL420DzGOa42homOIiyKoOI3wWZw%3D%3D"

BASE = "http://apis.data.go.kr/B552061/frequentzoneBicycle/getRestFrequentzoneBicycle"
ROWS = 1   # 스캔은 1건만 확인하면 됨(속도↑)
TIMEOUT = 15

# 전국 시도 코드(17개 고정)
SIDO_ALL = [11,26,27,28,29,30,31,36,41,42,43,44,45,46,47,48,50]

def build_url():
    return f"{BASE}?serviceKey={SERVICE_KEY}"

def check_count(year, sido, gugu):
    """해당 (연도, 시도, 구군)에 데이터 있는지 totalCount만 빠르게 확인"""
    url = build_url()
    params = {
        "type": "json",
        "searchYearCd": year,
        "siDo": sido,
        "guGun": gugu,
        "numOfRows": ROWS,
        "pageNo": 1,
    }
    r = requests.get(url, params=params, timeout=TIMEOUT)
    if r.status_code != 200:
        return 0
    text = r.text.lstrip()
    if text.startswith("<"):
        # XML 에러(키/라우팅 등)면 0으로 처리
        return 0
    try:
        data = r.json()
    except Exception:
        return 0

    # 새 포맷: body.totalCount
    body = (data.get("response") or {}).get("body") or {}
    tc = body.get("totalCount")
    if tc is not None:
        try:
            return int(tc)
        except Exception:
            return 0

    # 구형 포맷: items만 오는 경우
    items = (data.get("items") or {}).get("item")
    if items is None:
        return 0
    if isinstance(items, list):
        return len(items)
    return 1

def main():
    # 사용법: python scan_hotspots.py 2021   (연도 미입력이면 2021)
    year = 2021
    if len(sys.argv) >= 2:
        try:
            year = int(sys.argv[1])
        except:
            pass

    out_csv = f"areas_{year}.csv"
    print(f"[SCAN] year={year} → {out_csv} 로 저장")

    found = []
    # 구군 코드는 보통 1~399에 몰려 있음. 넉넉히 1..400 스캔.
    for sido in SIDO_ALL:
        print(f"\n[SI/DO] {sido} 스캔 시작…")
        for gugu in range(1, 401):
            try:
                tc = check_count(year, sido, gugu)
                if tc > 0:
                    print(f"  ▶ FOUND: siDo={sido}, guGun={gugu}, totalCount={tc}")
                    found.append((sido, gugu, tc))
                # 너무 빠른 호출 방지 살짝 쉬기
                time.sleep(0.05)
            except Exception:
                # 네트워크/일시 오류는 지나감
                pass

    # CSV 저장
    with open(out_csv, "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["siDo", "guGun", "totalCount"])
        w.writerows(found)

    print(f"\n[FIN] year={year} 총 {len(found)}개 구군 발견 → {out_csv}")

if __name__ == "__main__":
    main()
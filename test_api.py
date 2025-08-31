# test_api.py
import requests, textwrap

# ✅ HTTPS 대신 HTTP로 (SSL 문제 우회)
BASE = "http://apis.data.go.kr/B552061/frequentzoneBicycle/getRestFrequentzoneBicycle"

# ✅ 반드시 공공포털의 "인코딩된 키" 전체 문자열을 그대로 넣으세요 (이미 % 기호가 들어있는 형태)
SERVICE_KEY = "RKqzhkFffQBQois3%2F3fNLq6CvB7O3BwmNxzFxoVJV8B%2FhgWdf2pXePTFxkNL420DzGOa42homOIiyKoOI3wWZw%3D%3D"

# serviceKey는 URL 본문에 직접 붙이고, 나머지는 params로 → 이중 인코딩 방지
url = f"{BASE}?serviceKey={SERVICE_KEY}"
params = {
    "type": "json",
    "searchYearCd": 2021,
    "siDo": 11,
    "guGun": 110,
    "numOfRows": 10,
    "pageNo": 1,
}

try:
    r = requests.get(url, params=params, timeout=20)  # HTTP이므로 verify 옵션 불필요
    print("STATUS:", r.status_code)
    print("URL:", r.url)
    print("HEAD 200 chars:")
    print(textwrap.shorten(r.text.replace("\n"," "), width=200))
except Exception as e:
    print("EXC:", type(e).__name__, str(e))
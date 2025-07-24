# 🎵 Music Recommendation API (with CLIP Embedding)

이 프로젝트는 이미지 또는 텍스트로부터 CLIP 모델을 이용해 임베딩 벡터를 생성하고, 유사도를 기반으로 음악을 추천하는 API 서버입니다.

---

## 📦 환경 설정

### 1. 가상 환경 생성 및 활성화

```bash
python -m venv venv
.\venv\Scripts\Activate.ps1  # PowerShell 환경
```

> ⚠️ 윈도우 CMD를 사용할 경우: `venv\Scripts\activate.bat`

---

### 2. 필수 라이브러리 설치

```bash
pip install torch torchvision torchaudio
pip install transformers
pip install fastapi uvicorn pillow
pip install python-multipart
```

---

## 🚀 서버 실행

가상환경이 활성화된 상태에서 다음 명령어로 FastAPI 서버를 실행합니다:

```bash
uvicorn main:app --reload
```

서버가 성공적으로 실행되면 아래 주소에서 API 확인이 가능합니다:

* Swagger 문서: [http://127.0.0.1:8000/docs](http://127.0.0.1:8000/docs)

---

## 🎶 음악 샘플 데이터 임베딩 및 DB 삽입

### 1. 추가 라이브러리 설치 (DB 연동)

```bash
pip install mysql-connector-python
```

> 이미 `torch`와 `transformers`는 설치된 상태여야 합니다.

### 2. 임베딩 수행 및 삽입 실행

```bash
python embed_and_insert.py
```

이 스크립트는 `sample_music` 리스트를 CLIP 모델로 임베딩하고, 데이터베이스에 저장합니다.

---

## 📬 API 엔드포인트 요약

| 메서드  | 엔드포인트          | 설명              |
| ---- | -------------- | --------------- |
| POST | `/embed/text`  | 텍스트 → 임베딩 벡터 추출 |
| POST | `/embed/image` | 이미지 → 임베딩 벡터 추출 |

---

## ✅ 요구사항

* Python 3.8 이상
* CUDA 사용 권장 (GPU 있을 경우)
* MySQL 서버 (DB 연결 정보는 `embed_and_insert.py`에서 수정)

---

필요 시 `embed_and_insert.py`, FastAPI `main.py`, Java 클라이언트 통신 구조 예시 등도 함께 정리해드릴 수 있습니다. 추가로 원하시면 알려주세요!

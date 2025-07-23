import mysql.connector
from transformers import CLIPProcessor, CLIPModel
import torch
from sample_music_data import sample_music

print("시작")

model = CLIPModel.from_pretrained("openai/clip-vit-base-patch32")
processor = CLIPProcessor.from_pretrained("openai/clip-vit-base-patch32")

print("db 연결")
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="1234",
    database="picmuup_db"
)

print("db 연결 완료")

cursor = conn.cursor()

insert_query = """
INSERT INTO music (title, artist, genre, emotion_tags, embedding, youtube_url)
VALUES (%s, %s, %s, %s, %s, %s)
"""

print("삽입 시작")
for music in sample_music:
    text = f"{music['genre']} {music['emotion_tags']} {music['description']}"
    inputs = processor(text=[text], return_tensors="pt")
    with torch.no_grad():
        features = model.get_text_features(**inputs)
    embedding = features[0].tolist()

    cursor.execute(insert_query, (
        music["title"],
        music["artist"],
        music["genre"],
        music["emotion_tags"],
        str(embedding),
        music["youtube_url"]
    ))

conn.commit()
cursor.close()
conn.close()
print("삽입 완료!")
from fastapi import FastAPI, UploadFile
from pydantic import BaseModel
from transformers import CLIPProcessor, CLIPModel
from PIL import Image
import torch
import io

app = FastAPI()

model = CLIPModel.from_pretrained("openai/clip-vit-base-patch32")
processor = CLIPProcessor.from_pretrained("openai/clip-vit-base-patch32", use_fast=True)

class TextRequest(BaseModel):
    text: str

@app.post("/embed/image")
async def embed_image(file: UploadFile):
    image_bytes = await file.read()
    image = Image.open(io.BytesIO(image_bytes)).convert("RGB")
    inputs = processor(images=image, return_tensors="pt")
    outputs = model.get_image_features(**inputs)
    embedding = outputs[0].detach().numpy().tolist()
    return {"embedding": embedding}

@app.post("/embed/text")
async def embed_text(request: TextRequest):
    inputs = processor(text=[request.text], return_tensors="pt")
    outputs = model.get_text_features(**inputs)
    embedding = outputs[0].detach().numpy().tolist()
    return {"embedding": embedding}

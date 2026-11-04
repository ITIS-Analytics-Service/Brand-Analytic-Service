# Model Service API (внутренний, processing → model-service)

### 1)
```http request
POST /internal/v1/analyze
```

**Request:**
```json
{
  "id": "aa0e8400-...",
  "text": "..."
}
```

**Response 200:**
```json
{ 
  "id": "aa0e8400-...",
  "label": "POSITIVE",
  "score": 0.87
}
```

_400_ — пустой текст, _503_ — модель не загружена.

### 2)
```http request
POST /internal/v1/analyze-batch
```

**Request:**
```json
{ 
  "items": [
    {
      "id":"1",
      "text":"..."
    },
    {
      "id": "2",
      "text": "dadsadada"
    }
  ] }
```
(max 100).

**Response 200:**
```json
{
  "results": [
    {
      "id":"1",
      "label":"POSITIVE",
      "score":0.87
    },
    {
      "id":"2",
      "label":"NEUTRAL",
      "score":0.50
    }
  ]
}
```

### 3)
```http request
GET /internal/v1/health
```

**Response 200:**
```json
{ 
  "status": "UP",
  "modelLoadedAt": "..."
}
```


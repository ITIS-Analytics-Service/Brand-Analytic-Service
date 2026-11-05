# Processing Service API (публичный, через gateway)

## 1. Mentions

### 1.1)

```http request
GET /mentions
```

**Query:**


| Параметр         | Тип      | Описание                                                      |
|------------------|----------|---------------------------------------------------------------|
| q                | String   | Полнотекстовый по _title_ + _text_                            |
| sentiment        | Enum     | _POSITIVE_/_NEGATIVE_/_NEUTRAL_ (можно через запятую) |
| sourceId         | String   | _web_search_, _vk_search_ и др.                               |
| domain           | String   | _example.com_                                                 |
| from, to         | ISO-8601 | по _publishedAt_                                              |
| jobId            | UUID     | только из конкретного сеанса                                  |
| hasDate          | boolean  | _true_/_false_ - фильтр по наличию _publishedAt_              |
| page, size, sort | -        | default по _publishedAt, desc_                                |

**Response 200:**
```json
{
  "content": [
    {
      "mentionId": "aa0e8400-...",
      "brandId": "770e8400-...",
      "jobId": "990e8400-...",
      "sourceId": "web_search",
      "url": "https://vk.com/...",
      "title": "Обзор кроссовок Nike",
      "snippet": "Кроссовки оказались удобными, но...",
      "author": "ivan_petrov",
      "publishedAt": "2026-11-02T14:23:00Z",
      "collectedAt": "2026-11-04T11:01:12Z",
      "sentiment": {
        "label": "POSITIVE",
        "score": 0.82,
        "modelVersion": "v1-lexicon"
      }
    }
  ],
  "page": 0, "size": 20, "totalElements": 137, "totalPages": 7
}
```
_publishedAt_ может быть _null_ (источник не отдал). Такие упоминания видны в списке, но не попадают в timeseries.

### 1.2)
```http request
GET /mentions/{mentionId}
```

**Response 200:**
```json
{
  "mentionId": "aa0e8400-...",
  "brandId": "770e8400-...",
  "jobId": "990e8400-...",
  "sourceId": "web_search",
  "externalId": "hash-of-normalized-url",
  "url": "https://vk.com/...",
  "title": "Обзор кроссовок Nike",
  "text": "Полный текст публикации...",
  "author": "ivan_petrov",
  "publishedAt": "2026-11-02T14:23:00Z",
  "collectedAt": "2026-11-04T11:01:12Z",
  "processedAt": "2026-11-04T11:01:15Z",
  "sentiment": {
    "label": "POSITIVE",
    "score": 0.82,
    "modelVersion": "v1-lexicon"
  }
  "themes": ["performance", "price"]
}
```

**Ошибки:** _404 NOT_FOUND_

## 2) Analytics

### 2.1)
```http request
GET /analytics/overview
```

**Query:** _from_, _to_ (default - последние 7 дней; не больше 90 дней).

**Response 200:**
```json
{
  "period": {"from": "2026-10-28T00:00:00Z", "to": "2026-11-04T00:00:00Z"},
  "dataCoverage": {
    "from": "2026-10-01T00:00:00Z",
    "to": "2026-11-04T11:00:00Z"
  },
  "totalMentions": 342,
  "mentionsWithoutDate": 5,
  "sentimentBreakdown": {
    "POSITIVE": 198,
    "NEUTRAL": 97,
    "NEGATIVE": 40,
    "MIXED": 7
  },
  "bySource": [
    {"sourceId": "web_search", "count": 251, "positive": 148, "negative": 28},
    {"sourceId": "rss", "count": 91, "positive": 50, "negative": 12}
  ],
  "topDomains": [
    {"domain": "habr.com", "count": 45},
    {"domain": "vc.ru", "count": 22}
  ]
}
```

_dataCoverage_ — фактический диапазон, по которому есть данные. Frontend рисует график только в этих границах и пишет «данные с 1 октября».

_mentionsWithoutDate_ — сколько упоминаний не вошло в timeseries из-за отсутствия _publishedAt_.

### 2.2)

```http request
GET /analytics/timeseries
```

**Query:**

| Параметр     | Тип      | Описание                     |
|--------------|----------|------------------------------|
| _interval_   | Enum     | _day_ / _week_               |
| _groupBy_    | Enum     | _publishedAt_/ _collectedAt_ |
| _from_, _to_ | ISO-8601 | defualt - 30 дней; max - 90  |
| _sentiment_  | Enum     | опционально, фильтр                       |

**Response 200:**
```json
{
  "interval": "day",
  "groupBy": "publishedAt",
  "dataCoverage": {"from": "2026-10-01", "to": "2026-11-04"},
  "points": [
    {"date": "2026-10-28", "total": 41, "positive": 24, "negative": 6, "neutral": 11, "mixed": 0},
    {"date": "2026-10-29", "total": 52, "positive": 30, "negative": 9, "neutral": 13, "mixed": 0}
  ]
}
```
Пустые дни в _dataCoverage_ возвращаются с _total: 0_. Дни вне _dataCoverage_ **не возвращаются** — frontend сам решает, показать их серым или нет.

### 2.3)

```http request
GET /analytics/top-mentions
```

**Query:** _limit_ (default 10, max 50), _sentiment_ (опц.), _from_, _to_.

**Response 200**: список кратких объектов, как в GET /mentions.

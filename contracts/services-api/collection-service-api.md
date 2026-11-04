# Collection Service API

## 1. Monitoring Config

### 1.1) Получение данных о конфигурации мониторинга

```http request
GET /brands/me
```
**Response 200**:
```json
{
  "id": 1,
  "brandId": "uuid",
  "brandName": "ITIS",
  "intervalMinutes": 60,
  "monitoringActive": true,
  "selectedSources": ["rss", "web_search"],
  "lastCoveredUntil": "2026-11-04T11:00:00Z",
  "nextRunAt": "2026-11-04T12:00:00Z",
  "monitoringStartedAt": "2026-11-01T09:00:00Z",
  "createdAt": "2026-11-01T09:00:00Z",
  "updatedAt": "2026-11-04T08:00:00Z"
}
```

**Поля**:
- id - идентификатор определенного конфига для мониторинга;
- brandId - ;
- brandName - String. Храним название нашего бренда;
- intervalMinutes - null означает «только ручные запуски, Scheduler пропускает этот бренд»;
- monitoringActive — false ставит мониторинг на паузу;
- lastCoveredUntil — докуда у нас есть непрерывное покрытие (MAX из успешных until);
- nextRunAt — null, если intervalMinutes == null или monitoringActive == false;
- monitoringStartedAt — когда пользователь впервые создал конфиг (для UI).

**Response 404**: конфиг ещё не создан.


### 1.2) Создать или обновить информацию о конфигурации мониторинга

```http request
PUT /brands/me
```

```json
{
  "brandName": "ITIS",
  "intervalMinutes": 60,
  "monitoringActive": true,
  "selectedSources": ["rss", "web_search", "Telegram", "VK"]
}
```
**Валидация**:

- brandName: 1...100 символов;
- intervalMinutes: null или 5..1440;
- selectedSources: каждый d существует в source_catalog;
- При создании: lastCoveredUntil = null, nextRunAt = null (Scheduler выставит после первого завершённого job'а.

## 2) Keywords

### 2.1)

```http request
GET /brands/me/keywords
```

**Response 200**:
```json
[
  {"id": 1, "text": "ITIS", "matchType": "WORD", "createdAt": "2026-11-01T09:01:00Z"},
  {"id": 2, "text": "ИТИС", "matchType": "WORD", "createdAt": "2026-11-01T09:01:00Z"},
  {"id": 3, "text": "Я хочу кушать", "matchType": "PHRASE", "createdAt": "2026-11-01T09:02:00Z"}
]
```
### 2.2) Полная замена списка ключевых слов

```http request
PUT /brands/me/keywords
```
**Request**:

```json
{
  "keywords": [
    {"text": "ITIS", "matchType": "WORD"},
    {"text": "ИТИС", "matchType": "WORD"}
  ]
}
```
**Валидация**:
- 1..50 элементов;
- text: 1..100 символов, не пустой;
- Дубликаты - 400.

**Response 200**: массив как в GET.

## 3) Исключаем свои источники

### 3.1)
```http request
GET /brands/me/exclusions
```

**Response 200**:
```json
[
  {
    "id": 1,
    "type": "DOMAIN",
    "value": "our-brand.com",
    "createdAt": "2026-11-02T10:00:00Z"
  },
  {
    "id": 2,
    "type": "URL_PREFIX",
    "value": "https://vk.com/our-brand",
    "createdAt": "2026-11-02T10:01:00Z"
  }
]
```

### 3.2) 

```http request
POST /brands/me/exclusions
```

**Request**:
```json
{
  "type": "URL_PREFIX",
  "value": "https://vk.com/our-brand"
}
```

**Response 201**:
```json
{
  "id": 3,
  "type": "URL_PREFIX",
  "value": "https://vk.com/our-brand",
  "createdAt": "2026-11-04T12:00:00Z"
}
```

**Валидация**:
- type: DOMAIN | URL_PREFIX | URL_EXACT;
- value:
  - DOMAIN — hostname без scheme и path; 
  - URL_PREFIX/URL_EXACT — валидный URL с http(s).
- Дубликат (type + value) - 409 CONFLICT.

### 3.3)

```http request
DELETE /brands/me/exclusions/{id}
```

**Response 204**. 404 - не существует или чужое.

## 4) Sources (Источники)

### 4.1) 
Справочник (общий, не per-user).

```http request
GET /soruces
```

**Response 200**:
```json
[
  {
    "id": "rss",
    "name": "RSS",
    "type": "RSS",
    "enabled": true,
    "description": "Публичные RSS-ленты"
  },
  {
    "id": "web_search",
    "name": "Web Search",
    "type": "WEB_SEARCH",
    "enabled": true,
    "description": "Поиск через внешний поисковый API"
  }
]
```
Только enabled=true допустимы в PUT /brands/me.

## 5) Monitoring Jobs

### 5.1) Ручной запуск сеанса. Три формы тела.

```http request
POST /monitoring-jobs
```
- **а) Без периода — «проверь сейчас» (delta от lastCoveredUntil):**
```json
{ "trigger": "MANUAL" }
```
→ since = lastCoveredUntil ?? now - 30d, until = now.

- **b) С периодом — анализ конкретного отрезка:**
```json
{
  "trigger": "MANUAL",
  "since": "2026-10-01T00:00:00Z",
  "until": "2026-10-15T23:59:59Z"
}
```
- **c) Backfill при первом запуске — то же, что (b); frontend сам считает since = now - N дней.**

**Валидация:**
- *since < until*, иначе 400;
- _until <= now + 5min_ (clock skew), иначе 400;
- _until - since <= 90 дней_, иначе 400.
- Либо оба _since+until_, либо ни одного, иначе 400.

**Response 202:**
```json
{
  "jobId": "990e8400-e29b-41d4-a716-446655440222",
  "trigger": "MANUAL",
  "status": "PENDING",
  "requestedSince": "2026-10-01T00:00:00Z",
  "requestedUntil": "2026-10-15T23:59:59Z",
  "createdAt": "2026-11-04T12:00:00Z",
  "links": {
    "self": "/api/v1/monitoring-jobs/990e8400-e29b-41d4-a716-446655440222"
  }
}
```

**Ошибки:**
- _409 CONFLICT_ - уже есть _PENDING/RUNNING_ job для бренда. Тело содержит jobId текущего:
```json
{
  "code": "CONFLICT",
  "message": "Collection job already running",
  "details": [{"field": "jobId", "issue": "990e8400-..."}]
}
```
- _400 VALIDATION_ERROR_ - нет активного _MonitoringCOnfig_ или пустые ключевые слова, либо невалидный период.

### 5.2) История сеансов

```http request
GET /monitoring-jobs
```
**Query:**

| Параметр               | Тип      | Описание                                        |
|------------------------|----------|-------------------------------------------------|
| trigger                | enum     | SCHEDULED/MANUAL                                |
| status                 | enum     | фильтр по статусу(можно несколько через запятую |
| requestedSinceFrom     | ISO-8601 | jobs, чей requestedSince >= X                   |
| requestUntilTO         | ISO-8601 | jobs, чей requestedSince <= X                   |
| startedFrom, startedTo | ISO-8601 | по startedAt                                    |
| page, size, sort       | -        | default startedAt, desc                         |  

**Response 200:**
```json
{
  "content": [
    {
      "jobId": "990e8400-...",
      "trigger": "SCHEDULED",
      "status": "COMPLETED",
      "requestedSince": "2026-11-04T10:00:00Z",
      "requestedUntil": "2026-11-04T11:00:00Z",
      "startedAt": "2026-11-04T11:00:00Z",
      "finishedAt": "2026-11-04T11:02:13Z",
      "collectedCount": 37
    },
    {
      "jobId": "880e8400-...",
      "trigger": "MANUAL",
      "status": "PARTIALLY_COMPLETED",
      "requestedSince": "2026-10-01T00:00:00Z",
      "requestedUntil": "2026-10-15T23:59:59Z",
      "startedAt": "2026-11-03T14:30:00Z",
      "finishedAt": "2026-11-03T14:32:13Z",
      "collectedCount": 342
    }
  ],
  "page": 0, "size": 20, "totalElements": 2, "totalPages": 1
}
```

### 5.3) 
```http request
GET /monitoring-jobs/{jobId}
```

**Response 200:**
```json
{
  "jobId": "990e8400-...",
  "trigger": "SCHEDULED",
  "status": "PARTIALLY_COMPLETED",
  "requestedSince": "2026-11-04T10:00:00Z",
  "requestedUntil": "2026-11-04T11:00:00Z",
  "startedAt": "2026-11-04T11:00:00Z",
  "finishedAt": "2026-11-04T11:02:13Z",
  "collectedCount": 37,
  "errorMessage": "1 of 2 sources failed",
  "sources": [
    {
      "sourceId": "web_search",
      "status": "SUCCESS",
      "collectedCount": 29,
      "errorMessage": null,
      "details": {
        "searchStatus": "SUCCESS",
        "searchHits": 50,
        "fetched": 45,
        "extracted": 29,
        "excludedByRules": 7,
        "excludedDetails": [
          {"rule": "DOMAIN:our-brand.com", "count": 5},
          {"rule": "URL_PREFIX:https://vk.com/our-brand", "count": 2}
        ]
      }
    },
    {
      "sourceId": "rss",
      "status": "RATE_LIMITED",
      "collectedCount": 8,
      "errorMessage": "HTTP 429, retry after 60s",
      "details": {
        "feedUrl": "https://rss.com/ru/rss/...",
        "httpStatus": 429
      }
    }
  ]
}
```

**Ошибки:** _404 NOT_FOUND_.


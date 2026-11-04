# Auth API  (публичный, через gateway)
## 1. Регистрация пользователя
```http request
POST  /auth/register
```

Request:
```json
{
  "email": "user@example.com",
  "password": "kakoy-to_parol'"
}
```

Response 201:

```json
{
  "userId": "uuid",
  "email": "user@example.com",
  "createdAt": "2077-11-04T12:00:00Z"
}
```

## 2. Логинизация

```http request
POST /auth/login
```

Request:
```json
{
  "email": "user@example.com",
  "password": "kakoy-to_parol'"
}
```
Ошибки: 409 CONFLICT — email занят.

Response 200:
```json
{
  "token": "...",
  "expiresAt": "2077-11-11T12:00:00Z",
  "userId": "uuid"
}
```

Ошибки: 401 UNAUTHORIZED

## Выход 

```http request
POST /auth/logout
```


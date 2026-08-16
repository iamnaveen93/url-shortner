# URL Shortener

A simple URL shortening service built with **Java 21, Spring Boot, MySQL, Redis, and Docker**.

## Problems We Are Solving

* Convert long URLs into short, easy-to-share URLs.
* Redirect users from a short code to the original URL.
* Avoid generating duplicate short codes.
* Reduce database load when resolving frequently accessed short URLs.
* Automatically expire short URLs after a configurable period.

## How It Works

### Create Short URL

```text
Long URL
   ↓
Generate shortCode
   ↓
Check DB for shortCode
   ↓
If exists → generate another
   ↓
Save URL + shortCode
   ↓
Return short URL
```

### Redirect

```text
shortCode
   ↓
Check Redis cache
   ↓
Cache hit → redirect
   ↓
Cache miss
   ↓
Check MySQL
   ↓
Store result in Redis
   ↓
Redirect
```

### Why Check the Database for `shortCode`?

The generated `shortCode` must be unique.

Before saving a new short URL, we check MySQL to make sure the generated code does not already exist. If it exists, we generate another code.

MySQL is the **source of truth** for URL mappings.

## Cache Implementation

Redis is used to cache frequently accessed short codes.

```text
GET /abc123
     ↓
   Redis
     ↓
 Cache hit ──────→ Redirect
     │
 Cache miss
     ↓
   MySQL
     ↓
 Store in Redis
     ↓
 Redirect
```

This avoids hitting MySQL for every redirect and improves response time for popular URLs.

## Prerequisites

* Java 21+
* Docker
* Gradle

## Run MySQL

```bash
docker run --name shortlink-mysql \
  -e MYSQL_ROOT_PASSWORD=rootpass \
  -e MYSQL_DATABASE=shortlink_db \
  -e MYSQL_USER=shortlink_user \
  -e MYSQL_PASSWORD=shortlink_pass \
  -p 3306:3306 \
  -d mysql:8.0.36
```

## Run Redis

```bash
docker run --name shortlink-redis \
  -p 6379:6379 \
  -d redis:7.4
```

## Build Docker Image

```bash
docker build -t url-shortener:latest .
```

## Run Application

```bash
./gradlew bootRun
```

Application runs on:

```text
http://localhost:8080
```

## API

### Create Short URL

```http
POST /api/v1/url
Content-Type: application/json
```

```json
{
  "longURL": "https://example.com/very/long/path"
}
```

### Redirect

```http
GET /{shortCode}
```

Returns `302 Found` and redirects to the original URL.

## Configuration

The main configuration is available in `application.properties`.

* `expiry_days` — number of days before a short URL expires.
* MySQL connection — datasource configuration.
* Redis connection — Redis host and port.

# Backend API — Curl Commands

All curl commands for testing the Invoice Backend API.

**Base URL:** `http://localhost:8080`

> **Note:** Make sure the backend is running first:
> ```shell
> ./mvnw quarkus:dev
> ```

---

## 1. Health Check

```shell
curl -s http://localhost:8080/hello
```

**Response:** `Hello from Quarkus REST`

---

## 2. Calculate Invoice Total

### 2.1 Happy Path — Multiple Currencies

Convert line items in **USD** and **GBP** into **EUR**:

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "EUR",
      "date": "2024-12-01",
      "lines": [
        {
          "description": "Consulting services",
          "currency": "USD",
          "amount": 1000.00
        },
        {
          "description": "Software license",
          "currency": "EUR",
          "amount": 500.00
        },
        {
          "description": "Hosting fees",
          "currency": "GBP",
          "amount": 200.00
        }
      ]
    }
  }'
```

**Expected response (200 OK):**
```json
{
  "total": 1767.45
}
```
*(Actual total depends on live exchange rates.)*

### 2.2 Same Currency — No Conversion Needed

All line items are already in the invoice's base currency:

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "USD",
      "date": "2024-12-01",
      "lines": [
        {
          "description": "Product A",
          "currency": "USD",
          "amount": 150.00
        },
        {
          "description": "Product B",
          "currency": "USD",
          "amount": 75.50
        },
        {
          "description": "Shipping",
          "currency": "USD",
          "amount": 12.99
        }
      ]
    }
  }'
```

**Expected response (200 OK):**
```json
{
  "total": 238.49
}
```

### 2.3 Convert EUR → USD

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "USD",
      "date": "2024-12-01",
      "lines": [
        {
          "description": "European consulting",
          "currency": "EUR",
          "amount": 2500.00
        }
      ]
    }
  }'
```

**Expected response (200 OK):**
```json
{
  "total": 2625.00
}
```
*(Actual total depends on live EUR→USD rate.)*

### 2.4 Single Line Item

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "GBP",
      "date": "2025-01-15",
      "lines": [
        {
          "description": "Web development",
          "currency": "USD",
          "amount": 3500.00
        }
      ]
    }
  }'
```

---

## 3. Error Cases

### 3.1 Missing Invoice Body

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{}'
```

**Response (400 Bad Request):**
```json
{
  "error": "Invoice is required"
}
```

### 3.2 Missing Currency

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "date": "2024-12-01",
      "lines": [
        {
          "description": "Test item",
          "currency": "USD",
          "amount": 100.00
        }
      ]
    }
  }'
```

**Response (400 Bad Request):**
```json
{
  "error": "Currency is required"
}
```

### 3.3 Missing Date

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "EUR",
      "lines": [
        {
          "description": "Test item",
          "currency": "USD",
          "amount": 100.00
        }
      ]
    }
  }'
```

**Response (400 Bad Request):**
```json
{
  "error": "Date is required"
}
```

### 3.4 Empty Line Items

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "EUR",
      "date": "2024-12-01",
      "lines": []
    }
  }'
```

**Response (400 Bad Request):**
```json
{
  "error": "At least one line item is required"
}
```

### 3.5 Missing Lines Field

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "EUR",
      "date": "2024-12-01"
    }
  }'
```

**Response (400 Bad Request):**
```json
{
  "error": "At least one line item is required"
}
```

### 3.6 Null Request Body

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d 'null'
```

**Response (400 Bad Request):**
```json
{
  "error": "Invoice is required"
}
```

### 3.7 Invalid Currency (Rate Not Found)

```shell
curl -s -X POST http://localhost:8080/invoice/total \
  -H "Content-Type: application/json" \
  -d '{
    "invoice": {
      "currency": "XYZ",
      "date": "2024-12-01",
      "lines": [
        {
          "description": "Test item",
          "currency": "USD",
          "amount": 100.00
        }
      ]
    }
  }'
```

**Response (404 Not Found):**
```json
{
  "error": "Exchange rate not found for USD -> XYZ"
}
```

---

## 4. Frankfurter API (External — for reference)

The backend calls the [Frankfurter API](https://www.frankfurter.app) internally. You can test it directly:

```shell
# Get EUR → USD rate for a specific date
curl -s "https://api.frankfurter.dev/2024-12-01?from=EUR&to=USD"
```

**Response:**
```json
{
  "amount": 1.0,
  "base": "EUR",
  "date": "2024-12-01",
  "rates": {
    "USD": 1.05
  }
}
```

```shell
# Get latest EUR rates
curl -s "https://api.frankfurter.dev/latest?from=EUR"
```

---

## Quick Reference

| # | Endpoint | Method | Purpose | Status Codes |
|---|----------|--------|---------|-------------|
| 1 | `/hello` | GET | Health check / Greeting | 200 |
| 2 | `/invoice/total` | POST | Calculate total with currency conversion | 200, 400, 404, 500 |
| 3 | `api.frankfurter.dev/{date}` | GET | Fetch exchange rates (external) | 200 |

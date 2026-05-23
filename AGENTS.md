<!-- BEGIN:project-agent-rules -->

# Invoice Backend — Agent Guide

A full-stack invoice total calculator with automatic currency conversion. Built with **Quarkus** (Java backend) and **Next.js** (React frontend with MUI).

The backend accepts an invoice with line items in different currencies, converts them to a target currency using [Frankfurter API](https://www.frankfurter.app/) exchange rates, and returns the total.

---

## Architecture

```
┌────────────────────┐      POST /invoice/total      ┌──────────────────────┐
│                    │ ──────────────────────────►   │                      │
│   Next.js UI       │                               │   Quarkus Backend    │
│   (localhost:3000) │ ◄──────────────────────────   │   (localhost:8080)   │
│                    │       JSON response            │                      │
└────────────────────┘                               └───────┬──────────────┘
                                                              │
                                                              │  GET /latest
                                                              ▼
                                                   ┌──────────────────────┐
                                                   │  Frankfurter API     │
                                                   │  (exchange rates)    │
                                                   └──────────────────────┘
```

---

## Tech Stack

| Layer      | Technology                                                |
|------------|-----------------------------------------------------------|
| Backend    | Quarkus 3.35, Java 25, RESTEasy Reactive, Jackson         |
| Frontend   | Next.js 16, React 19, TypeScript, MUI 9 (Material UI)    |
| API Client | Frankfurter API (exchange rates)                          |

---

## Project Structure

```
├── pom.xml                          # Maven build (backend)
├── mvnw / mvnw.cmd                  # Maven wrapper scripts
├── src/
│   ├── main/java/org/acme/
│   │   ├── InvoiceResource.java     # REST endpoint — POST /invoice/total
│   │   ├── InvoiceService.java      # Business logic — currency conversion + total calculation
│   │   └── FrankfurterApi.java      # REST client (REST Client Reactive) for Frankfurter exchange rates
│   ├── main/resources/
│   │   └── application.properties   # Quarkus config — server port, CORS, REST client base URL
│   └── test/java/org/acme/
│       ├── InvoiceResourceTest.java # Integration test for the REST endpoint
│       └── InvoiceServiceTest.java  # Unit test for the service layer
├── invoice-ui/                      # Next.js frontend (see invoice-ui/AGENTS.md for details)
│   ├── src/
│   │   ├── app/
│   │   │   ├── page.tsx             # Main page — invoice form, validation, submission
│   │   │   ├── layout.tsx           # Root layout — MUI providers chain
│   │   │   └── Providers.tsx        # MUI X date-picker localization (Day.js)
│   │   ├── types/invoice.ts         # TypeScript interfaces (LineItem, Invoice, etc.)
│   │   ├── services/api.ts          # API client — calculateInvoiceTotal()
│   │   └── theme.ts                 # MUI theme customization
│   └── .env.local                   # NEXT_PUBLIC_API_URL=http://localhost:8080
├── AGENTS.md                        # This file
├── README.md                        # Full project documentation
└── backlog/                          # Project backlog (tasks, documents)
```

---

## Backend — Key Conventions

### 1. REST endpoint (`InvoiceResource.java`)
- Single `@POST` endpoint at `/invoice/total`
- Accepts `InvoiceRequest` JSON, returns `InvoiceResponse` JSON
- Validates that the invoice has at least one line item and a valid currency
- Returns `400 Bad Request` with `{ "error": "..." }` on validation failure
- Returns `500 Internal Server Error` if exchange rate API fails

### 2. Business logic (`InvoiceService.java`)
- Converts each line item's amount from its currency to the invoice's base currency
- Uses `BigDecimal` for all monetary calculations (no floating-point precision loss)
- Rounds final total to 2 decimal places using `HALF_UP` rounding mode
- If a line item's currency matches the invoice currency, no conversion is needed (rate = 1.0)

### 3. Exchange rate client (`FrankfurterApi.java`)
- Uses Quarkus **REST Client Reactive** with `@RegisterRestClient` and `@RestClient`
- Calls `GET /latest?from={base}` on the Frankfurter API (https://api.frankfurter.dev)
- Returns a `Map<String, BigDecimal>` of currency codes → exchange rates
- The date from the invoice request is used so historical rates are respected (the Frankfurter API is called with `?from=`; for date-specific rates the endpoint would be `/latest` — the current implementation uses latest rates)

### 4. Error handling
- `InvoiceService` validates: non-null invoice, at least one line, non-empty currency
- Throws `IllegalArgumentException` on validation failures
- `InvoiceResource` catches exceptions and maps them to `400` or `500` JSON responses
- `FrankfurterApi` uses `@RegisterProvider` with an exception mapper for HTTP 4xx/5xx from Frankfurter

### 5. Tests
- **`InvoiceResourceTest.java`** — Quarkus integration test using `@QuarkusIntegrationTest`
  - Tests happy path with multiple currencies (USD, EUR, GBP → EUR)
  - Tests validation errors (empty invoice, missing lines)
  - Tests error handling when Frankfurter API is unreachable
- **`InvoiceServiceTest.java`** — Unit test using `@ExtendWith(MockitoExtension.class)`
  - Mocks the FrankfurterApi client
  - Tests currency conversion, same-currency passthrough, and validation

---

## Frontend — Quick Reference

See `invoice-ui/AGENTS.md` for the complete frontend agent guide.

| Aspect           | Detail                                         |
|------------------|-------------------------------------------------|
| Framework        | Next.js 16 (App Router), React 19               |
| UI Library       | MUI 9 (Material UI)                             |
| State            | All local `useState` in `page.tsx`              |
| API Call         | `calculateInvoiceTotal()` in `services/api.ts`  |
| Theme            | Blue primary, Teal secondary, Inter font        |
| Date Pickers     | MUI X with Day.js adapter                       |

---

## Build & Run Commands

```shell
# Backend
./mvnw quarkus:dev          # Start backend dev server (port 8080)
./mvnw test                 # Run backend tests

# Frontend
cd invoice-ui && npm run dev    # Start frontend dev server (port 3000)

# Both at once (two terminals)
# Terminal 1: ./mvnw quarkus:dev
# Terminal 2: cd invoice-ui && npm run dev
```

---

## Backend API Contract

| Method | Endpoint         | Request Body                              | Response Body               | Status Codes                  |
|--------|------------------|-------------------------------------------|-----------------------------|-------------------------------|
| POST   | /invoice/total   | `{ invoice: { currency, date, lines } }`  | `{ total: number }`         | 200 (OK), 400 (Bad Request), 500 (Server Error) |

---

## CORS

The backend is pre-configured to accept requests from `http://localhost:3000` (see `src/main/resources/application.properties`).

<!-- END:project-agent-rules -->

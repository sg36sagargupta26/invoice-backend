# Invoice Backend

A full-stack invoice total calculator with currency conversion. Built with **Quarkus** (Java backend) and **Next.js** (React frontend with MUI).

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

## Prerequisites

- **Java 25+** (the backend uses `maven.compiler.release` set to 25)
- **Maven** (or use the included `./mvnw` wrapper)
- **Node.js 20+** (for the frontend)
- **npm** or **yarn**

---

## Running the Backend (Quarkus)

```shell
# Start in dev mode with live reload
./mvnw quarkus:dev
```

The backend will be available at **http://localhost:8080**.

### Backend API

| Method | Endpoint           | Description                                           |
|--------|--------------------|-------------------------------------------------------|
| POST   | `/invoice/total`   | Calculate invoice total with currency conversion       |

#### Example Request

```json
{
  "invoice": {
    "currency": "EUR",
    "date": "2025-05-23",
    "lines": [
      { "description": "Consulting", "currency": "USD", "amount": 1000 },
      { "description": "Hosting",    "currency": "EUR", "amount": 200  },
      { "description": "Domain",     "currency": "GBP", "amount": 50   }
    ]
  }
}
```

#### Example Response

```json
{
  "total": 1278.45
}
```

### Running Backend Tests

```shell
./mvnw test
```

---

## Running the Frontend (Next.js)

```shell
cd invoice-ui

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

The frontend will be available at **http://localhost:3000**.

### Frontend Environment Variables

Create `invoice-ui/.env.local` (already provided):

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Building for Production

```shell
cd invoice-ui
npm run build
npm start
```

---

## Running Both Together (Quick Start)

Open **two terminal tabs**:

| Tab 1 — Backend                          | Tab 2 — Frontend                       |
|------------------------------------------|----------------------------------------|
| `./mvnw quarkus:dev`                     | `cd invoice-ui && npm run dev`         |

Then open **http://localhost:3000** in your browser.

---

## CORS Configuration

The backend is pre-configured to accept requests from `http://localhost:3000` (see `src/main/resources/application.properties`).

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
├── src/
│   ├── main/java/org/acme/
│   │   ├── InvoiceResource.java     # REST endpoint
│   │   ├── InvoiceService.java      # Business logic
│   │   └── FrankfurterApi.java      # REST client for exchange rates
│   └── test/java/org/acme/          # Backend tests
├── invoice-ui/
│   ├── package.json                 # Node deps (frontend)
│   ├── src/
│   │   ├── app/page.tsx             # Main page (invoice form)
│   │   ├── services/api.ts          # API service layer
│   │   ├── types/invoice.ts         # TypeScript interfaces
│   │   └── theme.ts                 # MUI theme customization
│   └── .env.local                   # API URL config
└── README.md
```

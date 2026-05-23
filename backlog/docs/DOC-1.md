---
id: DOC-1
title: Invoice Calculation Application - Implementation Plan
---

# Invoice Calculation Application - Implementation Plan

## Overview
Build a full-stack, multi-currency invoice calculation application with:
- **Backend**: Java Quarkus REST API (port 8080)
- **Frontend**: Next.js TypeScript + Material UI (port 3000)
- **External API**: Frankfurter.app for historical exchange rates

---

## Part 1: Backend Service (Quarkus Java)

### Architecture
```
src/main/java/org/acme/
├── model/
│   ├── InvoiceRequest.java      # Request POJO
│   ├── LineItem.java            # Line item POJO
│   └── FrankfurterResponse.java # Frankfurter API response model
├── client/
│   └── FrankfurterClient.java   # REST Client for Frankfurter.app
├── service/
│   └── InvoiceService.java      # Business logic & currency conversion
└── resource/
    └── InvoiceResource.java     # POST /invoice/total endpoint
```

### Task 1: Add Dependencies to pom.xml
- `quarkus-resteasy-reactive-jackson` - JSON serialization
- `quarkus-rest-client-jackson` - REST Client with JSON support
- `quarkus-rest-client` - REST Client base

### Task 2: Create Domain Models
- **InvoiceRequest**: Contains `Invoice` (currency, date) and `List<LineItem>`
- **LineItem**: description (String), currency (String), amount (BigDecimal)
- **FrankfurterResponse**: Maps the Frankfurter API JSON response (amount, base, date, rates map)

### Task 3: Create Frankfurter.app REST Client
- Interface with `@RegisterRestClient(baseUri = "https://api.frankfurter.dev")`
- Method: `getRates(@PathParam("date") String date, @QueryParam("from") String from, @QueryParam("to") String to)`
- Returns `FrankfurterResponse`
- Configure in `application.properties`: `org.acme.client.FrankfurterClient/mp-rest/url=https://api.frankfurter.dev`

### Task 4: Create InvoiceService
- `@ApplicationScoped` service
- For each line item:
  1. Fetch exchange rate from line currency → invoice currency via FrankfurterClient
  2. Round exchange rate to 4 decimal places
  3. Calculate: `lineTotal = round(amount * rate, 2)`
- Sum all line totals → final total rounded to 2 decimal places
- Handle cases where line currency == invoice currency (rate = 1.0)

### Task 5: Create InvoiceResource
- `@Path("/invoice")` with `POST` method at `/total`
- Accepts `InvoiceRequest` as JSON body
- Returns `text/plain` response
- Error handling:
  - `400`: Invalid/missing data → `"Error: Invalid request data"`
  - `404`: Exchange rate unavailable → `"Error: Exchange rate not found for ..."`
  - `500`: Unexpected errors → `"Error: Internal server error"`

### Task 6: Write Tests
- Unit test InvoiceService with mocked FrankfurterClient
- Integration test InvoiceResource with `@QuarkusTest`
- Test valid payload, missing fields, invalid currencies, API failures

---

## Part 2: Frontend Application (Next.js/TypeScript + MUI)

### Architecture
```
invoice-frontend/
├── src/
│   ├── app/
│   │   ├── page.tsx              # Main page with invoice form
│   │   ├── layout.tsx            # Root layout with MUI ThemeProvider
│   │   └── globals.css           # Global styles
│   ├── components/
│   │   ├── InvoiceForm.tsx       # Main invoice form component
│   │   ├── LineItemRow.tsx       # Single line item row (MUI)
│   │   └── ResultDisplay.tsx     # Display calculation result (MUI)
│   └── services/
│       └── invoiceApi.ts         # API service layer
```

### Task 7: Bootstrap Next.js App + MUI
- `npx create-next-app@latest invoice-frontend --typescript`
- Install MUI: `@mui/material`, `@emotion/react`, `@emotion/styled`, `@mui/x-date-pickers`, `dayjs`
- Configure MUI ThemeProvider in layout.tsx
- Configure DatePicker with LocalizationProvider (dayjs)

### Task 8: Create API Service Layer
- TypeScript interfaces matching backend models
- `POST /api/invoice/total` function using fetch
- Error handling for network errors and API error responses

### Task 9: Create Invoice Form UI (MUI Components)
- **Invoice Date**: MUI DatePicker (`@mui/x-date-pickers`)
- **Base Currency**: MUI TextField or Autocomplete (e.g., NZD, USD, AUD, EUR, GBP)
- **Dynamic Line Items**:
  - Each row: Description (TextField), Amount (TextField type="number"), Currency (TextField/Autocomplete)
  - Delete button per row (MUI IconButton with DeleteIcon)
  - "Add Line" button (MUI Button with AddIcon)
- **Calculate Total**: MUI Button (variant="contained", color="primary")
  - Shows MUI CircularProgress while loading
- **Result Display**: MUI Typography or Alert for success
- **Error Display**: MUI Alert (severity="error") or Snackbar

### Task 10: Style & Polish
- Custom MUI theme (colors, typography)
- Responsive layout with MUI Grid/Container
- Form validation (required fields, positive amounts, valid date)
- MUI Snackbar for success/error notifications
- Clean, professional UI

---

## Data Flow
```
User Input (MUI Form) 
    → API Service (TypeScript) 
    → HTTP POST /invoice/total (JSON) 
    → InvoiceResource (Java) 
    → InvoiceService (Java) 
    → FrankfurterClient (Java) 
    → Frankfurter.app API (External)
    → Response back through the chain
    → Display result on frontend (MUI components)
```

## Currency Conversion Logic
1. For each line item, get rate: `Frankfurter.app/{date}?from={lineCurrency}&to={invoiceCurrency}`
2. Round rate to 4 decimal places
3. Line total = `round(line.amount * rate, 2)`
4. Invoice total = `round(sum of all line totals, 2)`
5. Return total as plain text (e.g., `"1600.86"`)

## Error Handling Strategy
| Scenario | HTTP Status | Response Body |
|----------|-------------|---------------|
| Success | 200 | `1600.86` |
| Missing/invalid fields | 400 | `Error: Invalid request data` |
| Exchange rate not found | 404 | `Error: Exchange rate not found for USD to NZD on 2020-07-07` |
| Unexpected error | 500 | `Error: Internal server error` |

## MUI Component Mapping
| Requirement | MUI Component |
|-------------|---------------|
| Invoice Date | `DatePicker` from `@mui/x-date-pickers` |
| Currency inputs | `TextField` or `Autocomplete` |
| Description inputs | `TextField` |
| Amount inputs | `TextField type="number"` |
| Add Line button | `Button` with `AddIcon` |
| Delete Line button | `IconButton` with `DeleteIcon` |
| Calculate button | `Button` with `CircularProgress` |
| Result display | `Alert` (success) or `Typography` |
| Error display | `Alert` (error) or `Snackbar` |
| Layout | `Container`, `Grid`, `Paper`, `Stack` |


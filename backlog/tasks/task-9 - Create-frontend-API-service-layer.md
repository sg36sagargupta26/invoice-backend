---
id: task-9
title: Create frontend API service layer
status: Done
priority: high
milestone: "Part 2: Frontend Application (Next.js)"
assignee: []
created_date: '2026-05-23 06:35'
updated_date: '2026-05-23 09:45'
labels:
  - frontend
  - api
  - typescript
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Create a TypeScript service module that handles communication with the backend POST /invoice/total endpoint. Include proper typing (TypeScript interfaces) for the request and response. Handle loading states, error states, and successful responses.

## Acceptance Criteria

- [x] TypeScript interfaces for InvoiceRequest, LineItem, and response
- [x] API service function to POST invoice data
- [x] Proper error handling for network/API errors
- [x] CORS configuration on backend to allow frontend requests


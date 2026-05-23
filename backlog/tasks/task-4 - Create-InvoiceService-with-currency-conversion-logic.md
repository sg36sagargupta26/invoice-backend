---
id: task-4
title: Create InvoiceService with currency conversion logic
status: Done
priority: high
milestone: "Part 1: Backend Service (Quarkus)"
assignee: []
created_date: '2026-05-23 06:35'
updated_date: '2026-05-23 07:24'
labels:
  - backend
  - service
  - business-logic
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Create a service class (InvoiceService) that: 1) Takes the invoice request, 2) For each line item, fetches the exchange rate from the line's currency to the invoice currency using Frankfurter.app, 3) Converts each line amount, 4) Sums all converted amounts, 5) Rounds to 2 decimal places. Exchange rates must be rounded to 4 decimal places. Each line total and final total rounded to 2 decimal places.

## Acceptance Criteria

- [ ] InvoiceService class created with @ApplicationScoped
- [ ] Currency conversion logic using FrankfurterClient
- [ ] Exchange rates rounded to 4 decimal places
- [ ] Each line total rounded to 2 decimal places
- [ ] Final invoice total rounded to 2 decimal places
- [ ] Proper error handling for missing rates or API failures


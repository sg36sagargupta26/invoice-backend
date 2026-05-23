---
id: task-5
title: Create InvoiceResource REST endpoint
status: To Do
priority: high
milestone: "Part 1: Backend Service (Quarkus)"
assignee: []
created_date: '2026-05-23 06:35'
labels:
  - backend
  - rest
  - endpoint
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Create the REST resource class with @Path(\"/invoice\") and a POST method at \"/total\" that accepts the invoice JSON payload and returns the calculated total as text/plain. Handle error cases: 400 for invalid/missing data, 404 if exchange rates can't be fetched, 500 for unexpected errors. All errors prefixed with \"Error:\".

## Acceptance Criteria

- [ ] POST /invoice/total endpoint created
- [ ] Returns 200 with text/plain total rounded to 2 decimals
- [ ] Returns 400 with Error: prefix for invalid/missing data
- [ ] Returns 404 with Error: prefix when exchange rates unavailable
- [ ] Returns 500 with Error: prefix for unexpected errors
- [ ] Existing GreetingResource can be kept or removed


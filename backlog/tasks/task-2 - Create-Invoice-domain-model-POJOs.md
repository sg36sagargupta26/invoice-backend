---
id: task-2
title: Create Invoice domain model (POJOs)
status: To Do
priority: high
milestone: "Part 1: Backend Service (Quarkus)"
assignee: []
created_date: '2026-05-23 06:35'
labels:
  - backend
  - models
  - java
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Create Java POJO classes for the invoice request/response models: InvoiceRequest (with Invoice containing currency, date, and list of LineItem), LineItem (description, currency, amount), and InvoiceResponse (total as String/plain text).

## Acceptance Criteria

- [ ] InvoiceRequest class with nested Invoice and List<LineItem>
- [ ] LineItem class with description, currency, amount fields
- [ ] Proper Jackson annotations for JSON serialization/deserialization
- [ ] All classes use Java records or POJOs with getters/setters


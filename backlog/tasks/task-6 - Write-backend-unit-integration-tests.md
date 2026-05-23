---
id: task-6
title: Write backend unit & integration tests
status: Done
priority: medium
milestone: "Part 1: Backend Service (Quarkus)"
assignee: []
created_date: '2026-05-23 06:35'
updated_date: '2026-05-23 07:38'
labels:
  - backend
  - testing
  - quarkus
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Write Quarkus tests using @QuarkusTest for the InvoiceResource endpoint. Test with valid payloads, missing fields, invalid currencies, and verify the Frankfurter.app integration works. Use WireMock or Mockito to mock the Frankfurter API calls in unit tests.

## Acceptance Criteria

- [x] Test for valid invoice returns 200 with correct total
- [x] Test for missing fields returns 400
- [x] Test for invalid currency returns 404
- [x] Test for API failure returns 500
- [x] All tests pass with mvn test


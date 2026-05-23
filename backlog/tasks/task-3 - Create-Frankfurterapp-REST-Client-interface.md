---
id: task-3
title: Create Frankfurter.app REST Client interface
status: To Do
priority: high
milestone: "Part 1: Backend Service (Quarkus)"
assignee: []
created_date: '2026-05-23 06:35'
labels:
  - backend
  - rest-client
  - frankfurter
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Create a Quarkus REST Client interface (using @RegisterRestClient) to call the Frankfurter.app API (https://api.frankfurter.app) for fetching historical exchange rates. The endpoint should support fetching rates for a specific date and target currency.

## Acceptance Criteria

- [ ] FrankfurterClient interface created with @RegisterRestClient
- [ ] Method to fetch rates for a given date: /{date}?from={base}&to={target}
- [ ] Response model class for Frankfurter API response
- [ ] Application.properties configured with frankfurter URL
- [ ] REST client properly scoped as ApplicationScoped or Singleton


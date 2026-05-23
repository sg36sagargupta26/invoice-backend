---
id: task-11
title: Add Javadoc comments and documentation to all backend Java methods
status: Done
priority: medium
assignee: []
created_date: '2026-05-23 09:51'
updated_date: '2026-05-23 09:53'
labels:
  - documentation
  - javadoc
  - backend
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Add comprehensive Javadoc comments to all backend Java classes, methods, fields, and constructors across the entire project.

Files to document:
- `src/main/java/org/acme/service/InvoiceService.java`
- `src/main/java/org/acme/resource/InvoiceResource.java`
- `src/main/java/org/acme/client/FrankfurterClient.java`
- `src/main/java/org/acme/model/Invoice.java`
- `src/main/java/org/acme/model/LineItem.java`
- `src/main/java/org/acme/model/InvoiceRequest.java`
- `src/main/java/org/acme/model/FrankfurterResponse.java`
- `src/main/java/org/acme/GreetingResource.java`
- `src/test/java/org/acme/service/InvoiceServiceTest.java`
- `src/test/java/org/acme/resource/InvoiceResourceTest.java`

Each class/interface should have a `@param <T>` Javadoc explaining its purpose.
Each public method should document parameters (`@param`), return values (`@return`), and exceptions (`@throws`).
Getters/setters can use brief inline comments where the purpose is obvious.


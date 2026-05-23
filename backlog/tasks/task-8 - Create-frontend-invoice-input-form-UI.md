---
id: task-8
title: Create frontend invoice input form UI with MUI
status: Done
priority: high
milestone: "Part 2: Frontend Application (Next.js)"
assignee: []
created_date: '2026-05-23 06:35'
updated_date: '2026-05-23 08:42'
labels:
  - frontend
  - ui
  - react
dependencies: []
references: []
documentation: []
ordinal: 1000
---

Build a React component using Material UI components: 1) Invoice Date using MUI DatePicker (from @mui/x-date-pickers), 2) Base Currency using MUI TextField or Autocomplete, 3) Dynamic list of Invoice Lines with MUI components - each line has Description (TextField), Amount (TextField/number), Currency (TextField/Autocomplete), and a delete button (IconButton). Include an "Add Line" button to add more rows. A "Calculate Total" button to trigger the API call. Display the result from the backend response. If error, display error message gracefully.

## Acceptance Criteria

- [x] Invoice currency input field
- [x] Invoice date input field
- [x] Dynamic line items list with add/remove capability
- [x] Each line item has description, currency, amount fields
- [x] Submit button that calls backend API
- [x] Result display area for calculated total
- [x] Error display for API errors
- [x] MUI DatePicker for invoice date
- [x] MUI TextField/Autocomplete for base currency
- [x] Dynamic line items list with MUI components
- [x] Each line: Description (TextField), Amount (TextField), Currency (TextField/Autocomplete)
- [x] Add Line button to add new rows
- [x] Delete/Remove button per line item
- [x] Calculate Total button (MUI Button)
- [x] Result display area for calculated total
- [x] Error display for API errors using MUI Alert or Snackbar


---
id: task-8
title: Create frontend invoice input form UI with MUI
status: To Do
priority: high
milestone: "Part 2: Frontend Application (Next.js)"
assignee: []
created_date: '2026-05-23 06:35'
updated_date: '2026-05-23 07:16'
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

- [ ] Invoice currency input field
- [ ] Invoice date input field
- [ ] Dynamic line items list with add/remove capability
- [ ] Each line item has description, currency, amount fields
- [ ] Submit button that calls backend API
- [ ] Result display area for calculated total
- [ ] Error display for API errors
- [ ] MUI DatePicker for invoice date
- [ ] MUI TextField/Autocomplete for base currency
- [ ] Dynamic line items list with MUI components
- [ ] Each line: Description (TextField), Amount (TextField), Currency (TextField/Autocomplete)
- [ ] Add Line button to add new rows
- [ ] Delete/Remove button per line item
- [ ] Calculate Total button (MUI Button)
- [ ] Result display area for calculated total
- [ ] Error display for API errors using MUI Alert or Snackbar


<!-- BEGIN:project-agent-rules -->

# Invoice UI — Agent Guide

This is the frontend for the **Invoice Backend** project — a full-stack invoice total calculator with currency conversion.

## Tech Stack

| Technology | Version |
|------------|---------|
| Next.js    | 16.2.6  |
| React      | 19.2.4  |
| MUI (Material UI) | 9.0.1  |
| MUI X Date Pickers | 9.3.0 |
| Day.js     | 1.11.20 |
| TypeScript | 5.x     |
| Tailwind CSS | 4.x   |

## Project Structure

```
├── src/
│   ├── app/
│   │   ├── page.tsx          # Main page — Invoice form, validation, submission, result display
│   │   ├── layout.tsx        # Root layout — MUI ThemeProvider, CssBaseline, Providers wrapper
│   │   ├── Providers.tsx     # MUI X LocalizationProvider with Day.js adapter
│   │   ├── globals.css       # Global CSS (minimal; MUI handles most styling)
│   │   └── favicon.ico
│   ├── types/
│   │   ├── index.ts          # Barrel re-export of invoice types
│   │   └── invoice.ts        # All TypeScript interfaces (LineItem, Invoice, InvoiceRequest, etc.)
│   ├── services/
│   │   └── api.ts            # API service — calculateInvoiceTotal() function
│   └── theme.ts              # MUI theme — palette, typography, component overrides
├── .env.local                # NEXT_PUBLIC_API_URL=http://localhost:8080
├── next.config.ts            # Next.js config (minimal)
├── tsconfig.json             # Path alias @/ → ./src/*
├── package.json
├── AGENTS.md                 # This file
├── README.md                 # Frontend project docs
└── CLAUDE.md                 # Links to this file (@AGENTS.md)
```

## Key Conventions & Patterns

### 1. All state is in `page.tsx` (no state management library)
- `useState` for form fields, loading, errors, snackbar notifications
- `useCallback` for validation and handlers wrapped in `useCallback` to prevent unnecessary re-renders
- No React Context, Redux, or Zustand — the app is small enough to keep state local

### 2. Form validation
- A `validate()` function returns a boolean and populates a `FormErrors` state object
- `touched` flag is set to `true` on first submit, after which field-level errors are shown
- Errors clear individually when the user edits a field (not all at once)

### 3. API calls
- All backend communication goes through `src/services/api.ts`
- The `calculateInvoiceTotal()` function accepts an `InvoiceRequest` and returns `InvoiceResponse`
- Errors are caught and displayed in an Alert + Snackbar

### 4. Theme
- Defined in `src/theme.ts` using `createTheme()` from MUI
- Primary: Blue (#1565C0), Secondary: Teal (#00897B)
- Uses the "Inter" font family
- All radiuses: 12px (shape.borderRadius)
- Component overrides for Paper, TextField, Button, Table, AppBar, Snackbar, Alert

### 5. MUI X Date Pickers
- Uses `@mui/x-date-pickers` with `AdapterDayjs` (provided in `Providers.tsx`)
- `LocalizationProvider` wraps all page content in the root layout
- `DatePicker` component with `slotProps.textField` for error/helper text

### 6. Unused imports
- `InputLabel` and `FormHelperText` are imported in `page.tsx` but not directly used (they are used internally by MUI `Select` and `TextField` respectively). Keep them for correctness — MUI components may reference them.

## Build & Run Commands

```shell
npm install          # Install dependencies
npm run dev          # Start dev server (localhost:3000)
npm run build        # Production build
npm start            # Start production server
npm run lint         # ESLint
npx tsc --noEmit     # Type-check without emitting files
```

## Backend API Contract

| Method | Endpoint         | Request Body                          | Response Body       |
|--------|------------------|---------------------------------------|---------------------|
| POST   | /invoice/total   | `{ invoice: { currency, date, lines } }` | `{ total: number }` |

The backend runs on `http://localhost:8080` by default (configured in `.env.local`).

<!-- END:project-agent-rules -->

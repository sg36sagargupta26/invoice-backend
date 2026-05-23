# Invoice UI

The **Next.js** frontend for the Invoice Backend project — an invoice total calculator with automatic currency conversion.

Built with **Next.js 16**, **React 19**, **MUI 9** (Material UI), and **Day.js**.

---

## Features

- Multi-line invoice form with dynamic row add/remove
- Per-row currency selection (USD, EUR, GBP, JPY, CHF, CAD, AUD, INR)
- Invoice date picker (used for exchange rate lookup)
- Form validation with inline error messages
- Real-time backend calculation via Frankfurter API/Eurocentral Bank exchange rates
- Success/error feedback via snackbar notifications
- Responsive design (mobile-friendly MUI layout)

---

## Getting Started

### Prerequisites

- **Node.js 20+**
- **npm** or **yarn**
- The Quarkus backend running on `http://localhost:8080` (see the [root README](../README.md))

### Install & Run

```shell
# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

Open **http://localhost:3000** in your browser.

### Environment Variables

Create `invoice-ui/.env.local` if not already present:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## Project Structure

```
src/
├── app/
│   ├── page.tsx          # Main page — Invoice form, validation, submission, result display
│   ├── layout.tsx        # Root layout — MUI ThemeProvider, CssBaseline, Providers
│   ├── Providers.tsx     # MUI X date-picker localization (Day.js adapter)
│   └── globals.css       # Global CSS (minimal; MUI handles most styling)
├── types/
│   ├── index.ts          # Barrel re-export
│   └── invoice.ts        # TypeScript interfaces: LineItem, Invoice, InvoiceRequest, etc.
├── services/
│   └── api.ts            # API client — calculateInvoiceTotal()
└── theme.ts              # MUI theme — palette, typography, component overrides
```

---

## Available Scripts

| Command              | Description                        |
|----------------------|------------------------------------|
| `npm run dev`        | Start development server           |
| `npm run build`      | Create production build            |
| `npm start`          | Start production server            |
| `npm run lint`       | Run ESLint                         |
| `npx tsc --noEmit`   | Type-check without emitting files  |

---

## Tech Stack

| Library            | Version | Purpose                        |
|--------------------|---------|--------------------------------|
| Next.js            | 16.2.6  | React framework (App Router)   |
| React              | 19.2.4  | UI library                     |
| MUI (Material UI)  | 9.0.1   | Component library + theming    |
| MUI X Date Pickers | 9.3.0   | Date picker component          |
| Day.js             | 1.11.20 | Date manipulation library      |
| TypeScript         | 5.x     | Type safety                    |
| Tailwind CSS       | 4.x     | Utility CSS (minimal usage)    |

---

## Learn More

- [Next.js Documentation](https://nextjs.org/docs) — learn about Next.js features and API
- [MUI Documentation](https://mui.com/material-ui/) — component API and theming guide
- [Frankfurter API](https://www.frankfurter.app/) — exchange rate data source

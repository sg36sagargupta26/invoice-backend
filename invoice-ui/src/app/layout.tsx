import type { Metadata } from "next";
import { AppRouterCacheProvider } from '@mui/material-nextjs/v16-appRouter';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Providers from './Providers';
import theme from '@/theme';

/**
 * Next.js metadata for the Invoice Calculator application.
 *
 * @see https://nextjs.org/docs/app/building-your-application/optimizing/metadata
 */
export const metadata: Metadata = {
  title: "Invoice Calculator",
  description: "Calculate invoice totals with automatic currency conversion using live exchange rates.",
};

/**
 * Root layout component for the Next.js application.
 *
 * Sets up the following provider chain:
 * 1. **AppRouterCacheProvider** — MUI's Next.js App Router cache for server-side rendering.
 * 2. **ThemeProvider** — Injects the custom MUI theme (palette, typography, component overrides).
 * 3. **CssBaseline** — Normalises browser CSS and applies the MUI theme's background colour.
 * 4. **Providers** — Custom wrapper that provides the Day.js date adapter for MUI X DatePickers.
 *
 * @param props.children - The page content rendered by Next.js routing.
 * @returns The full HTML document with MUI providers wrapping the page content.
 */
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="h-full antialiased">
      <body className="min-h-full flex flex-col" style={{ margin: 0 }}>
        <AppRouterCacheProvider>
          <ThemeProvider theme={theme}>
            <CssBaseline />
            <Providers>
              {children}
            </Providers>
          </ThemeProvider>
        </AppRouterCacheProvider>
      </body>
    </html>
  );
}

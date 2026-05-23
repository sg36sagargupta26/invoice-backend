'use client';

import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

/**
 * Wraps the application children with MUI X date-picker providers.
 *
 * Sets up the {@link LocalizationProvider} with the {@link AdapterDayjs}
 * adapter so that {@link DatePicker} components throughout the app use
 * the <a href="https://day.js.org" target="_blank">Day.js</a> library
 * for date manipulation and formatting.
 *
 * @param props.children - The child component tree (typically the page content).
 * @returns The children wrapped in the localization context provider.
 */
export default function Providers({ children }: { children: React.ReactNode }) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      {children}
    </LocalizationProvider>
  );
}

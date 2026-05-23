import type { InvoiceRequest, InvoiceResponse, ErrorResponse } from '@/types';

/**
 * Base URL for the backend API, configurable via the `NEXT_PUBLIC_API_URL`
 * environment variable. Defaults to `http://localhost:8080` for local development.
 */
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

/**
 * Sends an invoice request to the backend to calculate the total with
 * automatic currency conversion.
 *
 * The backend fetches exchange rates from the European Central Bank
 * (via the Frankfurter API) for the invoice date and converts any
 * line items whose currency differs from the invoice's base currency.
 *
 * @param invoiceRequest - The request payload containing the invoice details
 *                         (base currency, date, and line items).
 * @returns The calculated total in the invoice's base currency.
 *
 * @throws {Error} If the backend returns a non-OK HTTP status, the error
 *                 message from the response body is thrown. Common failures:
 *                 - 400 Bad Request (missing/invalid fields)
 *                 - 404 Not Found (exchange rate not available)
 *                 - 500 Internal Server Error (unexpected server error)
 *
 * @example
 * ```ts
 * const result = await calculateInvoiceTotal({
 *   invoice: { currency: "USD", date: "2024-01-15", lines: [...] }
 * });
 * console.log(result.total); // 350.50
 * ```
 */
export async function calculateInvoiceTotal(
  invoiceRequest: InvoiceRequest
): Promise<InvoiceResponse> {
  const response = await fetch(`${API_BASE_URL}/invoice/total`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(invoiceRequest),
  });

  if (!response.ok) {
    const errorData: ErrorResponse = await response.json();
    throw new Error(errorData.error || 'Failed to calculate total');
  }

  return response.json();
}

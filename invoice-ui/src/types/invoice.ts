/**
 * Represents a single line item on an invoice.
 *
 * @property description - A brief description of the product or service (e.g. "Web Development").
 * @property currency    - ISO-4217 currency code for this line item (e.g. "USD", "EUR").
 * @property amount      - The monetary amount for this line item (e.g. 150.00).
 */
export interface LineItem {
  description: string;
  currency: string;
  amount: number;
}

/**
 * Represents an invoice with a base currency, an invoice date, and a list of line items.
 *
 * @property currency - The base (target) currency into which all line items will be converted.
 * @property date     - The invoice date in "YYYY-MM-DD" format, used for exchange rate lookup.
 * @property lines    - The line items on this invoice.
 */
export interface Invoice {
  currency: string;
  date: string;
  lines: LineItem[];
}

/**
 * Request payload sent to the backend `/invoice/total` endpoint.
 *
 * @property invoice - The invoice containing currency, date, and line items.
 */
export interface InvoiceRequest {
  invoice: Invoice;
}

/**
 * Successful response from the backend `/invoice/total` endpoint.
 *
 * @property total - The calculated total in the invoice's base currency, rounded to 2 decimal places.
 */
export interface InvoiceResponse {
  total: number;
}

/**
 * Error response from the backend when the request is invalid or calculation fails.
 *
 * @property error - A human-readable error message describing what went wrong.
 */
export interface ErrorResponse {
  error: string;
}

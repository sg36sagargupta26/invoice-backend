export interface LineItem {
  description: string;
  currency: string;
  amount: number;
}

export interface Invoice {
  currency: string;
  date: string;
  lines: LineItem[];
}

export interface InvoiceRequest {
  invoice: Invoice;
}

export interface InvoiceResponse {
  total: number;
}

export interface ErrorResponse {
  error: string;
}

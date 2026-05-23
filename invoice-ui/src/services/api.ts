import type { InvoiceRequest, InvoiceResponse, ErrorResponse } from '@/types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

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

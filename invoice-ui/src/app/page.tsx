'use client';

import { useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Paper,
  TextField,
  Button,
  Alert,
  CircularProgress,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Autocomplete,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import AddCircle from '@mui/icons-material/AddCircle';
import Delete from '@mui/icons-material/Delete';
import type { LineItem } from '@/types';
import { calculateInvoiceTotal } from '@/services/api';

const CURRENCIES = ['USD', 'EUR', 'GBP', 'JPY', 'CHF', 'CAD', 'AUD', 'INR'];

export default function Home() {
  const [baseCurrency, setBaseCurrency] = useState('USD');
  const [date, setDate] = useState(dayjs());
  const [lineItems, setLineItems] = useState<LineItem[]>([
    { description: '', currency: 'USD', amount: 0 },
  ]);
  const [total, setTotal] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleAddLine = () => {
    setLineItems([...lineItems, { description: '', currency: baseCurrency, amount: 0 }]);
  };

  const handleRemoveLine = (index: number) => {
    if (lineItems.length > 1) {
      setLineItems(lineItems.filter((_, i) => i !== index));
    }
  };

  const handleLineChange = (index: number, field: keyof LineItem, value: string | number) => {
    const updated = [...lineItems];
    (updated[index] as any)[field] = value;
    setLineItems(updated);
  };

  const handleSubmit = async () => {
    setLoading(true);
    setError(null);
    setTotal(null);

    try {
      const response = await calculateInvoiceTotal({
        invoice: {
          currency: baseCurrency,
          date: date.format('YYYY-MM-DD'),
          lines: lineItems.map((item) => ({
            ...item,
            amount: Number(item.amount),
          })),
        },
      });
      setTotal(response.total);
    } catch (err: any) {
      setError(err.message || 'Failed to calculate total');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
        Invoice Calculator
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
        Calculate invoice totals with automatic currency conversion using live exchange rates.
      </Typography>

      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" gutterBottom>
          Invoice Details
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, mb: 3, flexWrap: 'wrap' }}>
          <Autocomplete
            value={baseCurrency}
            onChange={(_event, newValue) => setBaseCurrency(newValue || 'USD')}
            options={CURRENCIES}
            sx={{ minWidth: 140 }}
            renderInput={(params) => (
              <TextField {...params} label="Currency" />
            )}
          />
          <DatePicker
            label="Invoice Date"
            value={date}
            onChange={(newValue) => setDate(newValue || dayjs())}
            slotProps={{ textField: { size: 'medium' } }}
          />
        </Box>

        <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 'medium' }}>
          Line Items
        </Typography>
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Description</TableCell>
                <TableCell>Currency</TableCell>
                <TableCell align="right">Amount</TableCell>
                <TableCell align="center" width={60}></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {lineItems.map((item, index) => (
                <TableRow key={index}>
                  <TableCell>
                    <TextField
                      size="small"
                      fullWidth
                      placeholder="Item description"
                      value={item.description}
                      onChange={(e) => handleLineChange(index, 'description', e.target.value)}
                    />
                  </TableCell>
                  <TableCell>
                    <FormControl size="small" fullWidth>
                      <Select
                        value={item.currency}
                        onChange={(e) => handleLineChange(index, 'currency', e.target.value)}
                      >
                        {CURRENCIES.map((c) => (
                          <MenuItem key={c} value={c}>{c}</MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                  </TableCell>
                  <TableCell align="right">
                    <TextField
                      size="small"
                      type="number"
                      value={item.amount || ''}
                      placeholder="0.00"
                      onChange={(e) => handleLineChange(index, 'amount', parseFloat(e.target.value) || 0)}
                      slotProps={{ htmlInput: { min: 0, step: 0.01 } }}
                      sx={{ maxWidth: 140 }}
                    />
                  </TableCell>
                  <TableCell align="center">
                    <IconButton
                      color="error"
                      size="small"
                      onClick={() => handleRemoveLine(index)}
                      disabled={lineItems.length === 1}
                    >
                      <Delete />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>

        <Button
          startIcon={<AddCircle />}
          onClick={handleAddLine}
          sx={{ mt: 1 }}
        >
          Add Line Item
        </Button>
      </Paper>

      <Box sx={{ display: 'flex', justifyContent: 'center', mb: 3 }}>
        <Button
          variant="contained"
          size="large"
          onClick={handleSubmit}
          disabled={loading}
          sx={{ minWidth: 200 }}
        >
          {loading ? <CircularProgress size={24} color="inherit" /> : 'Calculate Total'}
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {total !== null && (
        <Paper sx={{ p: 3, textAlign: 'center', bgcolor: 'success.light' }}>
          <Typography variant="h6" color="success.contrastText">
            Total in {baseCurrency}
          </Typography>
          <Typography variant="h3" sx={{ fontWeight: 'bold' }} color="success.contrastText">
            {total.toFixed(2)}
          </Typography>
        </Paper>
      )}
    </Container>
  );
}

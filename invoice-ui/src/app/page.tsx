'use client';

import { useState, useCallback } from 'react';
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
  FormHelperText,
  Autocomplete,
  AppBar,
  Toolbar,
  Grid,
  Snackbar,
  Chip,
  Tooltip,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import AddCircle from '@mui/icons-material/AddCircle';
import Delete from '@mui/icons-material/Delete';
import Receipt from '@mui/icons-material/Receipt';
import Calculate from '@mui/icons-material/Calculate';
import CurrencyExchange from '@mui/icons-material/CurrencyExchange';
import type { LineItem } from '@/types';
import { calculateInvoiceTotal } from '@/services/api';

const CURRENCIES = ['USD', 'EUR', 'GBP', 'JPY', 'CHF', 'CAD', 'AUD', 'INR'];

interface FormErrors {
  date?: string;
  description?: Record<number, string>;
  amount?: Record<number, string>;
}

export default function Home() {
  const [baseCurrency, setBaseCurrency] = useState('USD');
  const [date, setDate] = useState(dayjs());
  const [lineItems, setLineItems] = useState<LineItem[]>([
    { description: '', currency: 'USD', amount: 0 },
  ]);
  const [total, setTotal] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const [snackbarSeverity, setSnackbarSeverity] = useState<'success' | 'error'>('success');
  const [errors, setErrors] = useState<FormErrors>({});
  const [touched, setTouched] = useState(false);

  const validate = useCallback((): boolean => {
    const newErrors: FormErrors = {};
    let valid = true;

    // Validate date
    if (!date || !date.isValid()) {
      newErrors.date = 'Please select a valid date';
      valid = false;
    }

    // Validate line items
    const descriptionErrors: Record<number, string> = {};
    const amountErrors: Record<number, string> = {};

    lineItems.forEach((item, index) => {
      if (!item.description.trim()) {
        descriptionErrors[index] = 'Description is required';
        valid = false;
      }
      if (isNaN(item.amount) || item.amount <= 0) {
        amountErrors[index] = 'Amount must be greater than 0';
        valid = false;
      }
    });

    if (Object.keys(descriptionErrors).length > 0) {
      newErrors.description = descriptionErrors;
    }
    if (Object.keys(amountErrors).length > 0) {
      newErrors.amount = amountErrors;
    }

    setErrors(newErrors);
    setTouched(true);
    return valid;
  }, [date, lineItems]);

  const handleAddLine = () => {
    setLineItems([...lineItems, { description: '', currency: baseCurrency, amount: 0 }]);
    // Clear field-level errors for the new row
    if (errors.description) {
      const { [lineItems.length]: _, ...rest } = errors.description;
      setErrors(prev => ({ ...prev, description: Object.keys(rest).length > 0 ? rest : undefined }));
    }
    if (errors.amount) {
      const { [lineItems.length]: _, ...rest } = errors.amount;
      setErrors(prev => ({ ...prev, amount: Object.keys(rest).length > 0 ? rest : undefined }));
    }
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

    // Clear field error on change if touched
    if (touched) {
      if (field === 'description' && errors.description?.[index]) {
        setErrors(prev => {
          const desc = { ...prev.description };
          delete desc[index];
          return { ...prev, description: Object.keys(desc).length > 0 ? desc : undefined };
        });
      }
      if (field === 'amount' && errors.amount?.[index]) {
        setErrors(prev => {
          const amt = { ...prev.amount };
          delete amt[index];
          return { ...prev, amount: Object.keys(amt).length > 0 ? amt : undefined };
        });
      }
    }
  };

  const handleSubmit = async () => {
    if (!validate()) return;

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
      setSnackbarMessage(`Total calculated: ${response.total.toFixed(2)} ${baseCurrency}`);
      setSnackbarSeverity('success');
      setSnackbarOpen(true);
    } catch (err: any) {
      const message = err.message || 'Failed to calculate total. Please try again.';
      setError(message);
      setSnackbarMessage(message);
      setSnackbarSeverity('error');
      setSnackbarOpen(true);
    } finally {
      setLoading(false);
    }
  };

  const isFormValid = lineItems.some(item => item.description.trim() && item.amount > 0);

  return (
    <>
      <AppBar position="static" color="primary" elevation={0}>
        <Toolbar>
          <Receipt sx={{ mr: 1.5 }} />
          <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 700, letterSpacing: '0.02em' }}>
            Invoice Calculator
          </Typography>
          <Chip
            icon={<CurrencyExchange />}
            label="Live Exchange Rates"
            size="small"
            variant="outlined"
            sx={{
              color: 'rgba(255,255,255,0.9)',
              borderColor: 'rgba(255,255,255,0.4)',
              '& .MuiChip-icon': { color: 'rgba(255,255,255,0.7)' },
            }}
          />
        </Toolbar>
      </AppBar>

      <Container maxWidth="md" sx={{ py: 4 }}>
        <Paper sx={{ p: { xs: 2, sm: 3 }, mb: 3 }}>
          <Typography variant="h5" gutterBottom sx={{ mb: 0.5 }}>
            Invoice Details
          </Typography>
          <Typography variant="body2" sx={{ mb: 3 }}>
            Enter invoice metadata and line items below. The total will be calculated in your chosen base currency.
          </Typography>

          <Grid container spacing={2} sx={{ mb: 3 }}>
            <Grid size={{ xs: 12, sm: 6 }}>
              <Autocomplete
                value={baseCurrency}
                onChange={(_event, newValue) => setBaseCurrency(newValue || 'USD')}
                options={CURRENCIES}
                renderInput={(params) => (
                  <TextField {...params} label="Base Currency" helperText="All amounts will be converted to this currency" />
                )}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <DatePicker
                label="Invoice Date"
                value={date}
                onChange={(newValue) => {
                  setDate(newValue || dayjs());
                  if (touched && newValue?.isValid()) {
                    setErrors(prev => ({ ...prev, date: undefined }));
                  }
                }}
                slotProps={{
                  textField: {
                    fullWidth: true,
                    error: touched && !!errors.date,
                    helperText: touched && errors.date ? errors.date : 'Exchange rate for this date will be used',
                  },
                }}
              />
            </Grid>
          </Grid>

          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1.5 }}>
            <Typography variant="subtitle1" sx={{ mb: 0 }}>
              Line Items
            </Typography>
            <Button
              startIcon={<AddCircle />}
              size="small"
              onClick={handleAddLine}
            >
              Add Item
            </Button>
          </Box>

          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell sx={{ width: '40%' }}>Description</TableCell>
                  <TableCell sx={{ width: '20%' }}>Currency</TableCell>
                  <TableCell sx={{ width: '25%' }} align="right">Amount</TableCell>
                  <TableCell sx={{ width: '15%' }} align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {lineItems.map((item, index) => (
                  <TableRow
                    key={index}
                    sx={{
                      '&:last-child td': { borderBottom: 0 },
                      bgcolor: index % 2 === 0 ? 'transparent' : 'rgba(0,0,0,0.02)',
                    }}
                  >
                    <TableCell>
                      <TextField
                        size="small"
                        fullWidth
                        placeholder="e.g. Web Development"
                        value={item.description}
                        onChange={(e) => handleLineChange(index, 'description', e.target.value)}
                        error={touched && !!errors.description?.[index]}
                        helperText={touched && errors.description?.[index] ? errors.description[index] : ''}
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
                        error={touched && !!errors.amount?.[index]}
                        helperText={touched && errors.amount?.[index] ? errors.amount[index] : ''}
                        slotProps={{
                          htmlInput: { min: 0, step: 0.01, style: { textAlign: 'right' } },
                        }}
                        sx={{ maxWidth: 140 }}
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Tooltip title={lineItems.length === 1 ? 'At least one line item required' : 'Remove item'}>
                        <span>
                          <IconButton
                            color="error"
                            size="small"
                            onClick={() => handleRemoveLine(index)}
                            disabled={lineItems.length === 1}
                          >
                            <Delete fontSize="small" />
                          </IconButton>
                        </span>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 1 }}>
            <Typography variant="caption" color="text.secondary">
              {lineItems.length} line item{lineItems.length !== 1 ? 's' : ''}
            </Typography>
          </Box>
        </Paper>

        <Box sx={{ display: 'flex', justifyContent: 'center', mb: 3 }}>
          <Button
            variant="contained"
            size="large"
            onClick={handleSubmit}
            disabled={loading}
            startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <Calculate />}
            sx={{ minWidth: 240, py: 1.5 }}
          >
            {loading ? 'Calculating...' : 'Calculate Total'}
          </Button>
        </Box>

        <Grid container spacing={3}>
          <Grid size={{ xs: 12, md: 6 }}>
            {error && (
              <Alert
                severity="error"
                onClose={() => setError(null)}
                sx={{ '& .MuiAlert-message': { width: '100%' } }}
              >
                <Typography variant="subtitle2" gutterBottom>
                  Calculation Failed
                </Typography>
                <Typography variant="body2">
                  {error}
                </Typography>
              </Alert>
            )}
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            {total !== null && (
              <Paper
                sx={{
                  p: 3,
                  textAlign: 'center',
                  bgcolor: 'success.light',
                  border: '2px solid',
                  borderColor: 'success.main',
                }}
              >
                <Typography variant="subtitle1" color="success.dark" gutterBottom>
                  Total in {baseCurrency}
                </Typography>
                <Typography
                  variant="h3"
                  sx={{ fontWeight: 700, color: 'success.dark', fontFamily: '"Inter", monospace' }}
                >
                  {total.toFixed(2)}
                </Typography>
                <Typography variant="caption" color="success.dark" sx={{ mt: 1, display: 'block' }}>
                  Includes currency conversion at {date.format('MMM D, YYYY')} exchange rates
                </Typography>
              </Paper>
            )}
          </Grid>
        </Grid>
      </Container>

      <Box
        component="footer"
        sx={{
          py: 3,
          mt: 'auto',
          textAlign: 'center',
          borderTop: '1px solid',
          borderColor: 'divider',
          bgcolor: 'background.paper',
        }}
      >
        <Typography variant="body2" color="text.secondary">
          Exchange rates provided by <strong>Frankfurter API</strong> (European Central Bank)
        </Typography>
      </Box>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={4000}
        onClose={() => setSnackbarOpen(false)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert
          onClose={() => setSnackbarOpen(false)}
          severity={snackbarSeverity}
          variant="filled"
          sx={{ width: '100%' }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </>
  );
}

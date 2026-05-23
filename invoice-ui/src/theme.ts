'use client';
import { createTheme } from '@mui/material/styles';

/**
 * Application-wide MUI theme.
 *
 * **Palette choices:**
 * - Primary: Blue (#1565C0) — used for the app bar, primary buttons, and key interactive elements.
 * - Secondary: Teal (#00897B) — used for accents and secondary actions.
 * - Background: Light grey (#F5F7FA) page background with white (#FFFFFF) paper surfaces.
 * - Success/Error: Green and red with light tint variants for alerts and result displays.
 *
 * **Typography:**
 * - Uses the "Inter" font family (falling back to Segoe UI, Roboto, etc.).
 * - Heading variants (h4/h5/h6) have increased weight and tighter letter-spacing for a modern look.
 * - Subtitle1 and body2 are styled with dark grey tones for improved readability.
 *
 * **Component overrides:**
 * - All MuiPaper surfaces have a subtle box shadow (elevation 1).
 * - MuiTextField inputs use rounded corners (border-radius: 8px).
 * - MuiButtons are uppercased by default — overridden to `none` for a cleaner look,
 *   with elevated shadows on contained variants.
 * - Table headers have a light blue-grey background and bold text.
 * - Table cells, alerts, and snackbars share consistent rounding.
 * - The app bar shadow is reduced for a flatter, more modern aesthetic.
 */
const theme = createTheme({
  palette: {
    primary: {
      main: '#1565C0',
      light: '#5E9CEA',
      dark: '#0D47A1',
      contrastText: '#FFFFFF',
    },
    secondary: {
      main: '#00897B',
      light: '#4DB6AC',
      dark: '#00695C',
      contrastText: '#FFFFFF',
    },
    background: {
      default: '#F5F7FA',
      paper: '#FFFFFF',
    },
    success: {
      main: '#2E7D32',
      light: '#E8F5E9',
    },
    error: {
      main: '#D32F2F',
      light: '#FFEBEE',
    },
  },
  typography: {
    fontFamily: '"Inter", "Segoe UI", "Roboto", "Helvetica Neue", Arial, sans-serif',
    h4: {
      fontWeight: 700,
      letterSpacing: '-0.02em',
    },
    h5: {
      fontWeight: 600,
      letterSpacing: '-0.01em',
    },
    h6: {
      fontWeight: 600,
    },
    subtitle1: {
      fontWeight: 600,
      color: '#37474F',
    },
    body2: {
      color: '#546E7A',
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          backgroundColor: '#F5F7FA',
          minHeight: '100vh',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          boxShadow: '0 1px 3px rgba(0,0,0,0.08), 0 1px 2px rgba(0,0,0,0.06)',
        },
      },
    },
    MuiTextField: {
      defaultProps: {
        variant: 'outlined',
      },
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
          },
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          textTransform: 'none',
          fontWeight: 600,
          padding: '10px 24px',
        },
        contained: {
          boxShadow: '0 2px 8px rgba(21, 101, 192, 0.25)',
          '&:hover': {
            boxShadow: '0 4px 12px rgba(21, 101, 192, 0.35)',
          },
        },
      },
    },
    MuiTableHead: {
      styleOverrides: {
        root: {
          '& .MuiTableCell-head': {
            fontWeight: 600,
            color: '#455A64',
            backgroundColor: '#F8FAFD',
          },
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          borderBottomColor: '#ECEFF1',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          boxShadow: '0 1px 3px rgba(0,0,0,0.08)',
        },
      },
    },
    MuiSnackbarContent: {
      styleOverrides: {
        root: {
          borderRadius: 8,
        },
      },
    },
    MuiAlert: {
      styleOverrides: {
        root: {
          borderRadius: 8,
        },
      },
    },
  },
});

export default theme;

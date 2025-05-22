import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import '@fontsource/inter';

const root = ReactDOM.createRoot(document.getElementById('root'));

const theme = createTheme({
  typography:{
    h6: {
      fontWeight: 300,
    },
    h1: {
      fontFamily: 'Inter, sans-serif',
      fontWeight: 600,
    }
  },
});

root.render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <App />
    </ThemeProvider>
  </React.StrictMode>
);

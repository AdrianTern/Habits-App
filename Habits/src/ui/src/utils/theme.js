import { createTheme, responsiveFontSizes } from '@mui/material/styles';

const commonTheme = createTheme({
    typography:{
      fontFamily: 'Inter, sans-serif',
      h6: {
        fontWeight: 300,
      },
      h3: {
        fontFamily: 'Inter, sans-serif',
        fontWeight: 600,
      },
    },
    palette: {
      custom: {
          grey : '#6e6d6d',
          lightgrey: '#cac9c9',
          darkgrey: '#595858',
          darkred: '#c64f4f',
          darkgreen: '#25821f',
          lightmode: '#edd277',
          darkmode: '#8f88cf',
          black: '#000',
          white: '#fff',
          violet: '#8f88cf',
      },
  },
  });

  export const lightTheme = responsiveFontSizes(createTheme({
    ...commonTheme,
    palette: {
      ...commonTheme.palette,
      mode: 'light',
      primary: {
        main: "#000",
      },
      secondary: {
        main: '#fff',
      },
      text: {
        primary: '#000',
        secondary: 'rgba(0, 0, 0, 0.7)',
        disabled: 'rgba(0, 0, 0, 0.5)',
      },
      background: {
        default: '#fdfdfd',
        paper: '#fdfdfd '
      },
      custom: {
        ...commonTheme.palette.custom,
        borderColor: '#cac9c9',
      }
    },
  }));

  export const darkTheme = responsiveFontSizes(createTheme({
    ...commonTheme,
    palette: {
      ...commonTheme.palette,
      mode: 'dark',
      primary: {
        main: "#fff",
      },
      secondary: {
        main: '#000',
      },
      text: {
        primary: '#ffffff',
        secondary: 'rgba(255, 255, 255, 0.7)',
        disabled: 'rgba(255, 255, 255, 0.5)',
      },
      background: {
        default: '#121212',
        paper: '#121212',
      },
      custom: {
        ...commonTheme.palette.custom,
        borderColor: '#121212',
      }
    },
  }));

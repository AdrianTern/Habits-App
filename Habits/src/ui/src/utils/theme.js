import { createTheme, responsiveFontSizes } from '@mui/material/styles';

const theme = createTheme({
    palette: {
      primary: {
        main: "#000000",
      },
      secondary: {
        main: "#fff",
      },
        custom: {
            darkgrey: '#595858',
            verydarkgrey : '#201e1e',
            lightgrey: '#cac9c9',
            red: '#e98a74',
            darkred: '#c64f4f',
            green: '#1ec06a',
            darkgreen: '#25821f',
        },
    },
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
  });

  export default responsiveFontSizes(theme);
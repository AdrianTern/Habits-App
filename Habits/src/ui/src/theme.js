import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        custom: {
            darkgrey: '#595858',
            lightgrey: '#cac9c9',
            red: '#e98a74',
            green: '#1ec06a',
        },
    },
    typography:{
      h6: {
        fontWeight: 300,
      },
      h3: {
        fontFamily: 'Inter, sans-serif',
        fontWeight: 600,
      }
    },
  });

  export default theme;
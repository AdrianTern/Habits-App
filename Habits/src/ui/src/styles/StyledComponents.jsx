import { styled } from '@mui/material/styles';
import { Box, Button, IconButton, TextField } from '@mui/material';
// Common styled components

// Styled container for main body
export const MainBox = styled(Box)({
  margin: '0 auto',
  padding: '1rem',
});

// Styled container for the header
export const HeaderBox = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  marginTop: '35%',

  [theme.breakpoints.up('sm')]: {
    marginTop: '25%',
  },

  [theme.breakpoints.up('lg')]: {
    marginTop: '15%',
  },
}));

// Styled container for body with smaller width
export const SmallBodyBox = styled(Box)(({ theme }) => ({
  width: '50vw',
  height: '50vh',
  margin: '0 auto',
  overflow: 'auto',
  paddingTop: '1rem',

  [theme.breakpoints.up('sm')]: {
    width: '30vw',
  },

  [theme.breakpoints.up('lg')]: {
    width: '20vw',
  },
}));

// Styled container to align child in the middle
export const CenterBox = styled(Box)({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
});

// Styled button
export const PrimaryButton = styled(Button)(({ theme }) => ({
  borderRadius: '30px',
  backgroundColor: theme.palette.custom.violet,
  padding: '10px',
  textTransform: 'none',
  transition: 'all 0.3s ease',
  '&:hover': {
    opacity: 0.5,
  }
}));

// Styled button with hover animation
export const AnimateButton = styled(IconButton)({
  transition: 'all 0.3s ease',
  '&:hover': {
    transform: 'scale(1.30)',
  }
});

// Styled input field
export const InputField = styled(TextField)({
  "& .MuiOutlinedInput-root": {
    borderRadius: "30px",
  }
})
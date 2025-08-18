import {
  Box,
  Paper,
  Button,
  Fab,
  IconButton,
  TextField,
  Checkbox,
} from '@mui/material';
import { styled } from '@mui/material/styles';

// Styled container for main body
export const MainBox = styled(Box)({
  margin: '0 auto',
  padding: '1rem',
});

// Styled container for main body
export const BodyBox = styled(Paper)(({ theme }) => ({
  width: '90vw',
  height: '50vh',
  margin: '0 auto',
  border: '1px solid',
  borderColor: theme.palette.custom.borderColor,
  borderRadius: '20px',
  backgroundColor: theme.palette.secondary.main,
  padding: '2rem',
  overflow: 'auto',

  [theme.breakpoints.up('sm')]: {
    width: '80vw',
  },
}));

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

// Styled container for form inputs
export const InputBox = styled(Box)({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center,'
});

// Styled container for input checkbox
export const ButtonBox = styled(Box)(({ theme }) => ({
  border: '1px solid',
  borderColor: theme.palette.primary.main,
  borderRadius: '1rem',
  paddingRight: '1rem',
  backgroundColor: theme.palette.primary.main,
}));

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

// Styled button to add new task
export const AddButton = styled(Fab)(({ theme }) => ({
  color: theme.palette.secondary.main,
  backgroundColor: theme.palette.custom.violet,
  position: 'fixed',
  bottom: '1rem',
  right: '1rem',
  transition: 'all 0.3s ease',
  '&:hover': {
    transform: 'scale(1.10)',
    color: theme.palette.secondary.main,
    backgroundColor: theme.palette.custom.violet,
  }
}));

// Styled input field
export const InputField = styled(TextField)({
  "& .MuiOutlinedInput-root": {
    borderRadius: "30px",
  }
});

// Styled input check box
export const InputCheckBox = styled(Checkbox)(({ theme }) => ({
  color: theme.palette.custom.grey,
  '&.Mui-checked': {
    color: theme.palette.custom.darkgreen,
  }
}));
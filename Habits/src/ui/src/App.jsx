import TaskList from './components/TaskList';
import MainAppBar from './components/MainAppBar';
import { Typography, Box, Paper } from '@mui/material';
import './App.css';
import TaskChips from './components/TaskChips';
import { motion } from 'framer-motion';
import { styled } from '@mui/material/styles';

const MainBox = styled(Box)({
  height: '80vh',
  margin: '0 auto',
  padding: '1rem',
  marginTop: '20%'
})

const HeaderBox = styled(Box)({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
})

const ListBox = styled(Paper)(({ theme }) => ({
  maxWidth: '90vw',
  maxHeight: '70vh',
  boxSizing: 'border-box',
  margin: '0 auto',
  marginBottom: '2rem',
  padding: '1rem',
  backgroundColor: theme.palette.secondary.main,

  [theme.breakpoints.up('sm')]: {
    maxWidth: '80vw',
    maxHeight: '80vh',
  },
}))

function App() {
  return (
    <>
      <MainAppBar key="main-app-bar" />
      <MainBox component={motion.div} layout initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }}>
        <HeaderBox>
          <Typography variant='h3' align='center' gutterBottom >
            habits.
          </Typography>
          <TaskChips />
        </HeaderBox>
        <ListBox elevation={24} square={false}>
          <TaskList />
        </ListBox>
      </MainBox>
    </>
  );
}

export default App;

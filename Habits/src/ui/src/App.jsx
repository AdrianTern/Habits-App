import TaskList from './components/TaskList';
import MainAppBar from './components/MainAppBar';
import TaskChips from './components/TaskChips';
import TaskForm from "./components/TaskForm";
import { Typography, Box, Fab } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { styled } from '@mui/material/styles';
import { motion } from 'framer-motion';
import { useSettingsActions } from './hooks/settingsHooks';

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

const AddButton = styled(Fab)(({ theme }) => ({
  color: theme.palette.secondary.main,
  backgroundColor: theme.palette.primary.main,
  position: 'fixed',
  bottom: '1rem',
  right: '1rem',
  transition: 'all 0.3s ease',
  '&:hover': {
    transform: 'scale(1.10)',
    color: theme.palette.secondary.main,
    backgroundColor: theme.palette.primary.main,
  }
}))

function App() {
  const { handleOpenTaskForm } = useSettingsActions();

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
        <TaskList />
      </MainBox>
      <TaskForm />
      <AddButton onClick={() => handleOpenTaskForm(true)}>
        <AddIcon />
      </AddButton>
    </>
  );
}

export default App;

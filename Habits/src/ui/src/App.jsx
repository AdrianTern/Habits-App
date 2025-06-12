import TaskList from './components/TaskList';
import MainAppBar from './components/MainAppBar';
import TaskChips from './components/TaskChips';
import TaskForm from "./components/TaskForm";
import { Typography, Box, Fab, Paper } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { styled } from '@mui/material/styles';
import { useTaskActions } from './hooks/taskHooks';

const MainBox = styled(Box)({
  margin: '0 auto',
  padding: '1rem',
})

const HeaderBox = styled(Box)({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  marginTop: '15%',
})

const ListBox = styled(Paper)(({ theme }) => ({
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
  const { handleOpenTaskForm } = useTaskActions();

  return (
    <>
      <MainAppBar key="main-app-bar" />
      <MainBox>
        <HeaderBox>
          <Typography variant='h3' align='center' gutterBottom >
            habits.
          </Typography>
          <TaskChips />
        </HeaderBox>
        <ListBox elevation={24} >
          <TaskList />
        </ListBox>
      </MainBox>
      <TaskForm />
      <AddButton onClick={() => handleOpenTaskForm(true)}>
        <AddIcon />
      </AddButton>
    </>
  );
}

export default App;

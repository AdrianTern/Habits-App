import TaskList from '../components/TaskList';
import MainAppBar from '../components/MainAppBar';
import TaskChips from '../components/TaskChips';
import TaskForm from "../components/TaskForm";
import { Fab, Paper, Typography } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { styled } from '@mui/material/styles';
import { useTasks } from '../hooks/taskHooks';
import{ MainBox, HeaderBox } from '../styles/StyledComponents';

// Styled container for main body
const BodyBox = styled(Paper)(({ theme }) => ({
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

// Styled button to add new task
const AddButton = styled(Fab)(({ theme }) => ({
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
}))

const HomePage = () => {
  // Get relevant task actions
  const { setCurrentTask, openTaskForm } = useTasks();

  // Handler to open task form on click of AddButton
  const handleAddButton = () => {
    setCurrentTask(null);
    openTaskForm(true);
  }

  return (
    <>
      <MainAppBar />
      <MainBox>
        <HeaderBox>
          <Typography variant='h3' align='center' gutterBottom >
            habits.
          </Typography>
          <TaskChips />
        </HeaderBox>
        <BodyBox elevation={24} >
          <TaskList />
        </BodyBox>
      </MainBox>
      <TaskForm />
      <AddButton aria-label='add new task' onClick={handleAddButton}>
        <AddIcon />
      </AddButton>
    </>
  );
}

export default HomePage;

import TaskList from '../components/task/TaskList';
import MainAppBar from '../components/MainAppBar';
import TaskChips from '../components//task/TaskChips';
import TaskForm from "../components/task/TaskForm";
import { Typography } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useTasks } from '../hooks/taskHooks';
import{ BodyBox, MainBox, HeaderBox, AddButton } from '../styles/StyledComponents';

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
};

export default HomePage;

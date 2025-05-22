import TaskList from './components/TaskList';
import {Typography} from '@mui/material';
import * as api from './api';
import { useReducer } from 'react';
import { taskReducer } from './reducers/taskReducer';
import { useEffect } from 'react';

import './App.css';
function App() {
  // useReducer to manage the state of tasks
  const [tasks, dispatch] = useReducer(taskReducer, []);

  // Fetch tasks from the API when the component mounts
  const fetchTasks = async () => {
    try{
      const tasks = await api.getTasks();
      dispatch({
        type: 'SET_TASKS',
        payload: tasks
      })
    }
    catch (error) {
      console.error('Error fetching tasks:', error);
    }
  }

  // handleToggle function to toggle the completion status of a task
  const handleToggle = async (task ) => {
    try{
      const updatedTask = await api.toggleCompletion(task);
      dispatch({
        type: 'TOGGLE_TASK',
        payload: updatedTask
      })
    }
    catch (error) {
      console.error('Error toggling task:', error);
    }
  }

  // useEffect to fetch tasks when the component mounts
  useEffect(() => {
    fetchTasks();
  }, []);

  return (
    <div className="App">
      <Typography variant='h1' className='app-title' gutterBottom>Habits.</Typography>
      <TaskList tasks={tasks} onToggle={handleToggle}/>
    </div>
  );
}

export default App;

import TaskList from './components/TaskList';
import { Typography, Box, Paper } from '@mui/material';
import * as api from './api';
import { useReducer } from 'react';
import { taskReducer } from './reducers/taskReducer';
import { useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';

import './App.css';
import TaskChips from './components/TaskChips';
function App() {
  // useReducer to manage the state of tasks
  const [tasks, dispatch] = useReducer(taskReducer, []);

  const setTasks = (taskSet) => {
    dispatch({type: "SET_TASKS", payload: taskSet});
  }

  // Fetch tasks from the API when the component mounts
  const fetchTasks = async () => {
    try{
      const tasks = await api.getTasks();
      dispatch({
        type: 'SET_TASKS',
        payload: tasks,
      })
    }
    catch (error) {
      console.error('Error fetching tasks:', error);
    }
  }
  
  const handleAddTask = async (task) => {
    try{
      const newTask = await api.addTask(task);
      dispatch({
        type: 'ADD_TASK',
        payload: newTask
      })
    } catch(error){
        console.error("Failed to add task", error);
    }
  }

  const handleUpdateTask = async (task) => {
    try{
      const updatedTask = await api.updateTask(task);
      dispatch({
        type: 'UPDATE_TASK',
        payload: updatedTask
      })
    } catch(error){
        console.error("Failed to update task", error);
    }
  }

  const handleTaskDialogSave = async (isCreate, task) => {
    if(isCreate){
      handleAddTask(task);
    }else{
      handleUpdateTask(task);
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

  const handleDelete = async (taskId) => {
    try{
      await api.deleteTask(taskId);
      dispatch({
        type: 'DELETE_TASK',
        payload: taskId
      })
    } catch(error){
      console.error('Failed to delete task', error)
    }
  }

  // useEffect to fetch tasks when the component mounts
  useEffect(() => {
    fetchTasks();
  }, []);

  const MotionTypography = motion(Typography);

  return (
    <motion.div layout>
      <AnimatePresence>
        <Box 
          className="App"
          sx={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '80vh',
            margin: '0 auto'
          }}
        >
          <Box>
            <MotionTypography 
              initial={{ opacity: 0, y: -20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5 }}
              variant='h3' 
              className='app-title' 
              align='center'
              gutterBottom >
                habits.
            </MotionTypography>
            <TaskChips />
            <Paper 
              className="task-list-box"
              elevation={24}
              square={false}
              sx={{
                maxWidth: { xs: '90vw', sm: '70vw', md: '50vw'},
                maxHeight: { xs: '70vh', sm: '80vh'},
                boxSizing: 'border-box',
                margin: '0 auto',
                padding: '1rem',
              }}>
              <TaskList tasks={tasks} onToggle={handleToggle} onSave={handleTaskDialogSave} onDelete={handleDelete}/>
            </Paper>
          </Box>
        </Box>
      </AnimatePresence>
    </motion.div>
  );
}

export default App;

import TaskList from './components/TaskList';
import { Typography, Box, Paper } from '@mui/material';
import * as api from './api';
import { useReducer } from 'react';
import { taskReducer } from './reducers/taskReducer';
import { motion, AnimatePresence } from 'framer-motion';

import './App.css';
import TaskChips from './components/TaskChips';
function App() {
  // useReducer to manage the state of tasks
  const initialState = {
    tasks: [],
    filter: 'today',
  };
  const [state, dispatch] = useReducer(taskReducer, initialState);
  const{ tasks, filter } = state;

  const fetchTasks = async(newFilter) => {
    try{
      const tasks = await api.getTasks(newFilter);
      dispatch({
        type: 'SET_TASKS',
        payload: tasks,
      })
    } catch(error){
      console.error('Failed to fetch tasks', error);
    }
  }

  const handleSelectTaskChip = async (newFilter) => {
    dispatch({
      type: 'SET_FILTER',
      payload: newFilter,
    })
    fetchTasks(newFilter);
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
    fetchTasks(filter);
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
    fetchTasks(filter);
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
    fetchTasks(filter);
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
    fetchTasks(filter);
  }

  return (
    <motion.div>
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
            <motion.div layout>
              <Typography 
                variant='h3' 
                className='app-title' 
                align='center'
                gutterBottom >
                  habits.
              </Typography>
            </motion.div>
            <TaskChips onSelect={handleSelectTaskChip} />
            <Paper
              component={motion.div}
              layout
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

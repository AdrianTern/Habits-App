import TaskList from './components/TaskList';
import { Typography, Box, Paper } from '@mui/material';
import * as api from './api/api';
import { useReducer } from 'react';
import { taskReducer } from './reducers/taskReducer';
import { motion, AnimatePresence } from 'framer-motion';
import { isTaskMatchFilter } from './utils/isTaskMatchFilter'; 

import './App.css';
import TaskChips from './components/TaskChips';
function App() {
  // useReducer to manage the state of tasks
  const initialState = {
    tasks: [],
    filter: 'today',
    taskCount: {
      todayCount: 0,
      upcomingCount: 0,
      overdueCount: 0,
      allCount: 0,
    },
  };
  const [state, dispatch] = useReducer(taskReducer, initialState);
  const{ tasks, filter, taskCount } = state;

  const fetchTasks = async(newFilter) => {
    try{
      const result = await api.getTasks(newFilter);
      dispatch({
        type: 'SET_TASKS',
        payload: result,
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

  const dispatchTaskWithFilter = (actionType, task) => {
    if(isTaskMatchFilter(actionType, filter)){
      dispatch({
        type: actionType,
        payload: task,
      })
    }
  }

  const handleAddTask = async (task) => {
    try{
      const newTask = await api.addTask(task);
      dispatchTaskWithFilter('ADD_TASK', newTask);
      fetchTasks(filter);
    } catch(error){
        console.error("Failed to add task", error);
    }
  }

  const handleUpdateTask = async (task) => {
    try{
      const updatedTask = await api.updateTask(task);
      dispatchTaskWithFilter('UPDATE_TASK', updatedTask);
      fetchTasks(filter);
    } catch(error){
        console.error("Failed to update task", error);
    }
    fetchTasks(filter);
  }

  // handleToggle function to toggle the completion status of a task
  const handleToggle = async (task ) => {
    try{
      const updatedTask = await api.toggleCompletion(task);
      dispatchTaskWithFilter('TOGGLE_TASK', updatedTask);
      fetchTasks(filter);
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
      fetchTasks(filter);
    } catch(error){
      console.error('Failed to delete task', error)
    }
  }

  const handleTaskDialogSave = async (isCreate, task) => {
    if(isCreate){
      handleAddTask(task);
    }else{
      handleUpdateTask(task);
    }
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
            <TaskChips onSelect={handleSelectTaskChip} taskCount={taskCount} />
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

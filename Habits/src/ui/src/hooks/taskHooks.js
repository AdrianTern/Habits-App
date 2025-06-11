import { useContext } from "react";
import { TaskContext } from "../context/tasks/TaskContext";
import { isTaskMatchFilter } from '../utils/isTaskMatchFilter';
import * as api from '../api/api';

export function useTaskState() {
    const { state } = useContext(TaskContext);
    return state;
}

export function useTaskActions() {
    const state = useTaskState();
    const { dispatch } = useContext(TaskContext);

    const fetchTasks = async (newFilter) => {
        try {
            const result = await api.getTasks(newFilter);

            dispatch({
                type: 'SET_TASKS',
                payload: result,
            })
        } catch (error) {
            console.error('Failed to fetch tasks', error);
        }
    }

    const handleChangeTaskChip = async (newFilter) => {
        dispatch({
            type: 'SET_FILTER',
            payload: newFilter,
        })
        fetchTasks(newFilter);
    }

    const dispatchTaskWithFilter = (actionType, task) => {
        if (isTaskMatchFilter(actionType, state.filter)) {
            dispatch({
                type: actionType,
                payload: task,
            })
        }
    }

    const handleAddTask = async (task) => {
        try {
            const newTask = await api.addTask(task);
            dispatchTaskWithFilter('ADD_TASK', newTask);
            fetchTasks(state.filter);
        } catch (error) {
            console.error("Failed to add task", error);
        }
    }

    const handleUpdateTask = async (task) => {
        try {
            const updatedTask = await api.updateTask(task);
            dispatchTaskWithFilter('UPDATE_TASK', updatedTask);
            fetchTasks(state.filter);
        } catch (error) {
            console.error("Failed to update task", error);
        }
        fetchTasks(state.filter);
    }

    // handleToggle function to toggle the completion status of a task
    const handleToggleTask = async (task) => {
        try {
            const updatedTask = await api.toggleCompletion(task);
            dispatchTaskWithFilter('TOGGLE_TASK', updatedTask);
            fetchTasks(state.filter);
        }
        catch (error) {
            console.error('Error toggling task:', error);
        }
        fetchTasks(state.filter);
    }

    const handleDeleteTask = async (taskId) => {
        try {
            await api.deleteTask(taskId);
            dispatch({
                type: 'DELETE_TASK',
                payload: taskId
            })
            fetchTasks(state.filter);
        } catch (error) {
            console.error('Failed to delete task', error)
        }
    }

    const handleSaveTask = async (isCreate, task) => {
        if (isCreate) {
            handleAddTask(task);
        } else {
            handleUpdateTask(task);
        }
    }

    const handleSetCurrentTask = (task) => {
        dispatch({ type: 'SET_CURRENT_TASK', payload: task });
    }

    return {
        fetchTasks,
        handleChangeTaskChip,
        dispatchTaskWithFilter,
        handleAddTask,
        handleUpdateTask,
        handleToggleTask,
        handleDeleteTask,
        handleSaveTask,
        handleSetCurrentTask,
    };
}
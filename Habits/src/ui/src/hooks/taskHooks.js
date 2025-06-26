import { useContext } from "react";
import { TaskViewContext } from "../context/TaskViewContext";
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import * as api from '../api/taskAPI';

// Sort task by the following priority:
// 1. isCompleted: Incomplete task always stay at the top, completed task always stay at the bottom
// 2. isRoutineTask: Routine task should always stay at the top
// 3. dueDate: Task without due date comes first, then sort ascendingly
const sortTasks = (tasks) => {
    if (tasks) {
        return [...tasks].sort((a, b) => {
            if (a.isCompleted !== b.isCompleted) return a.isCompleted ? 1 : -1;
            if (a.routineDetailsResponse.isRoutineTask !== b.routineDetailsResponse.isRoutineTask) return a.routineDetailsResponse.isRoutineTask ? -1 : 1;
            return compareDates(a, b);
        });
    }
}

const compareDates = (a, b) => {
    if (!a.dueDate && !b.dueDate) return 0;
    if (!a.dueDate) return -1;
    if (!b.dueDate) return 1;

    return new Date(a.dueDate) - new Date(b.dueDate);
}

// Custom hook that returns task state
export function useTaskState() {
    const { state } = useContext(TaskViewContext);
    return state;
}

// Custom hook that provides actions to configure task states
export function useTasks() {
    const state = useTaskState();
    const queryClient = useQueryClient();
    const { dispatch } = useContext(TaskViewContext);

    // Fetch tasks according to selected filter
    const { data: tasksData, isLoading: tasksLoading, isError: tasksError, error: tasksErrorObj } = useQuery({
        queryKey: ['tasks', state.filter],
        queryFn: () => api.getTasks(state.filter),
        keepPreviousData: true,
    });

    // Fetch task count
    const { data: taskCountData } = useQuery({
        queryKey: ['tasks'],
        queryFn: () => api.getTaskCount(),
        keepPreviousData: true,
    });

    // Add task
    const addTask = useMutation({
        mutationFn: api.addTask,
        onSuccess: () => {
            queryClient.invalidateQueries(['tasks', state.filter]);
        }
    });

    // Update task
    const updateTask = useMutation({
        mutationFn: api.updateTask,
        onSuccess: () => {
            queryClient.invalidateQueries(['tasks', state.filter]);
        }
    });

    // Toggle task completion
    const toggleTask = useMutation({
        mutationFn: api.toggleCompletion,
        onSuccess: () => {
            queryClient.invalidateQueries(['tasks', state.filter]);
        }
    });

    // Delete task
    const deleteTask = useMutation({
        mutationFn: api.deleteTask,
        onSuccess: () => {
            queryClient.invalidateQueries(['tasks', state.filter]);
        }
    });

    // Wrapper function for either create/update task
    const saveTask = async (isCreate, task) => {
        (isCreate ? addTask : updateTask).mutate(task);
    };

    // Updates filter
    const selectFilter = async (newFilter) => {
        dispatch({ type: 'SET_FILTER', payload: newFilter })
    };

    // Set current selected task
    const setCurrentTask = (task) => {
        dispatch({ type: 'SET_CURRENT_TASK', payload: task });
    };

    // Open task form
    const openTaskForm = (newVal) => {
        dispatch({ type: 'OPEN_TASK_FORM', payload: newVal });
    };

    return {
        tasks: sortTasks(tasksData ?? []),
        taskCount: taskCountData ?? [],
        tasksLoading: tasksLoading,
        errorFetching: tasksError,
        tasksErrorObj: tasksErrorObj,
        toggleTask: toggleTask.mutate,
        deleteTask: deleteTask.mutate,
        saveTask,
        selectFilter,
        setCurrentTask,
        openTaskForm
    };
}
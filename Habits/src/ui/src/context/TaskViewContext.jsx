import { createContext, useReducer } from "react";
import { taskReducer } from "../reducers/taskReducer";

// Context for task 
export const TaskViewContext = createContext();

const initialState = {
    filter: 'all',
    currentTask: null,
    openTaskForm: false,
  };

// Provider component that provides actions to configure task states
export const TaskStateProvider = ({ children }) => {
    const[state, dispatch] = useReducer(taskReducer, initialState);

    return (
        <TaskViewContext.Provider value={{ state, dispatch }}>
            {children}
        </TaskViewContext.Provider>
    );
};
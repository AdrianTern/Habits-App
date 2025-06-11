import { createContext, useReducer } from "react";
import { taskReducer } from "./taskReducer";

export const TaskContext = createContext();

const initialState = {
    tasks: [],
    filter: 'today',
    taskCount: {
      todayCount: 0,
      upcomingCount: 0,
      overdueCount: 0,
      allCount: 0,
      routineCount: 0,
    },
    currentTask: null,
  };

export function TaskStateProvider({ children }){
    const[state, dispatch] = useReducer(taskReducer, initialState);

    return (
        <TaskContext.Provider value={{ state, dispatch }}>
            {children}
        </TaskContext.Provider>
    );
}
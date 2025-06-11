// useReducer for managing tasks in a todo list application
// This reducer function manages the state of tasks in a todo list application.
// It handles actions such as setting tasks, adding a task, updating a task, toggling a task's completion status, and deleting a task.

// Sort task by the following priority:
// 1. isCompleted: Incomplete task always stay at the top, completed task always stay at the bottom
// 2. isRoutineTask: Routine task should always stay at the top
// 3. dueDate: Task without due date comes first, then sort ascendingly
const sortTasks = (tasks) => {
    if(tasks){
        return [...tasks].sort((a,b) => {
            if(a.isCompleted !== b.isCompleted) return a.isCompleted ? 1 : -1;
            if(a.routineDetailsResponse.isRoutineTask !== b.routineDetailsResponse.isRoutineTask) return a.routineDetailsResponse.isRoutineTask ? -1 : 1;
            return compareDates(a,b);
        });
    }
  }

const compareDates = (a, b) => {
    if(!a.dueDate && !b.dueDate) return 0;
    if(!a.dueDate) return -1;
    if(!b.dueDate) return 1;

    return new Date(a.dueDate) - new Date(b.dueDate);
}

export const taskReducer = (state, action) => {
    switch(action.type) {
        case 'SET_TASKS':
            return {
                ...state, 
                tasks: sortTasks(action.payload.taskResponse),
                taskCount: {
                    todayCount: action.payload.taskCount.todayCount,
                    upcomingCount: action.payload.taskCount.upcomingCount,
                    overdueCount: action.payload.taskCount.overdueCount,
                    allCount: action.payload.taskCount.allCount,
                    routineCount: action.payload.taskCount.routineCount,
                }
            };
        case 'SET_FILTER':
            return { ...state, filter: action.payload };
        case 'SET_CURRENT_TASK':
            return{ ...state, currentTask: action.payload };
        case 'ADD_TASK':
            return { ...state, tasks: sortTasks([...state.tasks, action.payload]) };
        case 'UPDATE_TASK':
            return {
                ...state, 
                tasks: sortTasks(state.tasks.map(task =>
                    task.id === action.payload.id ? action.payload : task
                ))
            };
        case 'TOGGLE_TASK':
            return {
                ...state,
                tasks: sortTasks(state.tasks.map(task =>
                    task.id === action.payload.id ? { ...task, isCompleted: !task.isCompleted } : task
                )) 
            };
        case 'DELETE_TASK':
            return {
                ...state,
                tasks: sortTasks(state.tasks.filter( task => task.id !== action.payload ))
            };
        default:
            return state;   
    }
}
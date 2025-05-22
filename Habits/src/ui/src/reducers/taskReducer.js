// useReducer for managing tasks in a todo list application
// This reducer function manages the state of tasks in a todo list application.
// It handles actions such as setting tasks, adding a task, updating a task, toggling a task's completion status, and deleting a task.
export const taskReducer = (state, action) => {
    switch(action.type) {
        case 'SET_TASKS':
            return action.payload;
        case 'ADD_TASK':
            return [...state, action.payload];
        case 'UPDATE_TASK':
            return state.map(task =>
                task.id === action.payload.id ? action.payload : task
            );
        case 'TOGGLE_TASK':
            return state.map(task =>
                task.id === action.payload.id ? { ...task, isCompleted: !task.isCompleted } : task
            );
        case 'DELETE_TASK':
            return state.filter( task => task.id !== action.payload.id );
        default:
            return state;   
    }
}
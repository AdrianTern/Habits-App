// Reducer to manage the state of tasks in a todo list application.
export const taskReducer = (state, action) => {
    switch (action.type) {
        case 'SET_FILTER':
            return { ...state, filter: action.payload };
        case 'SET_CURRENT_TASK':
            return { ...state, currentTask: action.payload };
        case 'OPEN_TASK_FORM':
            return { ...state, openTaskForm: action.payload };
        default:
            return state;
    }
}
// useReducer for managing tasks in a todo list application
// This reducer function manages the state of tasks in a todo list application.
// It handles actions such as setting tasks, adding a task, updating a task, toggling a task's completion status, and deleting a task.
const sortTasks = (tasks) => {
    return [...tasks].sort((a,b) => {
      const isCompletedDiff = a.isCompleted - b.isCompleted;
      if(isCompletedDiff !== 0) return isCompletedDiff;

      const dueDateDiff = new Date(a.dueDate) - new Date(b.dueDate);
      if(dueDateDiff !== 0) return dueDateDiff;

      return a.title.localeCompare(b.title);
    });
  }

export const taskReducer = (state, action) => {
    switch(action.type) {
        case 'SET_TASKS':
            return sortTasks(action.payload);
        case 'ADD_TASK':
            return sortTasks([...state, action.payload]);
        case 'UPDATE_TASK':
            return sortTasks(state.map(task =>
                task.id === action.payload.id ? action.payload : task
            ));
        case 'TOGGLE_TASK':
            return sortTasks(state.map(task =>
                task.id === action.payload.id ? { ...task, isCompleted: !task.isCompleted } : task
            ));
        case 'DELETE_TASK':
            return sortTasks(state.filter( task => task.id !== action.payload ));
        default:
            return state;   
    }
}
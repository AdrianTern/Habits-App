import { fetchWithAuth } from "../utils/fetchWithAuth";

// Functions for task api calls
const BASE_URL = '/api/tasks';
const clientTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

// Get tasks
// need a loading page until jwt token is initialized
export const getTasks = async (filter = null) => {
    const url = `${BASE_URL}?filter=${filter}&timezone=${clientTimeZone}`;
    const response = await fetchWithAuth(url);

    if(!response.ok) throw new Error('Failed to fetch tasks');
    else if(response.status === 204) return [];  // Returns empty if no content

    return response.json();
}

// Get task count
export const getTaskCount = async () => {
    const url = `${BASE_URL}/taskCount?timeZone=${clientTimeZone}`;
    const response = await fetchWithAuth(url);

    if(!response.ok) throw new Error('Failed to fetch task count');
    else if(response.status === 204) return []; // Returns empty if no content

    return response.json();
}

// Add new task
export const addTask = async (task) => {
    const response = await fetchWithAuth(BASE_URL, {
        method: 'POST',
        body: JSON.stringify(task),
    });

    if(!response.status === 201) throw new Error('Failed to add task');

    return response.json();
}

// Update task
export const updateTask = async (task) => {
    const url = `${BASE_URL}/${task.id}`;
    const response = await fetchWithAuth(url, {
        method: 'PUT',
        body: JSON.stringify(task),
    });

    if(!response.status === 201) throw new Error('Failed to update task');

    return response.json();
}

// Toggle task completion
export const toggleCompletion = async (task) => {
    const url = `${BASE_URL}/${task.id}`;
    const response = await fetchWithAuth(url, {
        method: 'PATCH',
        body: JSON.stringify({
            isCompleted: !task.isCompleted
        }),
    });

    if(!response.ok) throw new Error('Failed to toggle task completion');

    return response.json();
}

// Delete task
export const deleteTask = async (taskId) => {
    const url = `${BASE_URL}/${taskId}`;
    const response = await fetchWithAuth(url, {
        method: 'DELETE',  
    });
    
    if(!response.status === 204) throw new Error('Failed to delete task');

    return;
}
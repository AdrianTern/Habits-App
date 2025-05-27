const BASE_URL = '/api/tasks';

// Get all tasks
export const getTasks = async (dueDate = null) => {
    let url = BASE_URL;
    if(dueDate && dueDate !== 'all'){
        url = `${BASE_URL}?dueDate=${dueDate}`;
    }
    const response = await fetch(url);

    if(!response.ok){
        throw new Error('Failed to fetch tasks');
    }

    if(response.status === 204) return [];

    return response.json();
}

// Add new task
export const addTask = async (task) => {
    const response = await fetch(BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(task),
    });

    if(!response.status === 201) {
        throw new Error('Failed to add task');
    }

    return response.json();
}

// Update task
export const updateTask = async (task) => {
    const response = await fetch(`${BASE_URL}/${task.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(task),
    });

    if(!response.ok){
        throw new Error('Failed to update task');
    }

    return response.json();
}

// Toggle task completion
export const toggleCompletion = async (task) => {
    const response = await fetch(`${BASE_URL}/${task.id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            isCompleted: !task.isCompleted
        }),
    });

    if(!response.ok){
        throw new Error('Failed to toggle task completion');
    }

    return response.json();
}

// Delete task
export const deleteTask = async (taskId) => {
    const response = await fetch(`${BASE_URL}/${taskId}`,{
        method: 'DELETE',  
    })

    if(!response.status === 204){
        throw new Error('Failed to delete task');
    }

    return taskId;
}
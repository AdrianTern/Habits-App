// Functions for task api calls
const BASE_URL = '/api/tasks';
const clientTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

// Get tasks
// need a loading page until jwt token is initialized
export const getTasks = async (filter = null) => {
    const user = localStorage.getItem("user");
    const jwt = JSON.parse(user);

    const url = `${BASE_URL}?filter=${filter}&timezone=${clientTimeZone}`;
    const response = await fetch(url, {
        headers: {
            "Authorization": `Bearer ${jwt.token}`,
        }
    });

    if(!response.ok) throw new Error('Failed to fetch tasks');
    else if(response.status === 204) return [];  // Returns empty if no content

    return response.json();
}

// Get task count
export const getTaskCount = async () => {
    const user = localStorage.getItem("user");
    const jwt = JSON.parse(user);

    const url = `${BASE_URL}/taskCount?timeZone=${clientTimeZone}`;
    const response = await fetch(url, {
        headers: {
            "Authorization": `Bearer ${jwt.token}`,
        }
    });

    if(!response.ok) throw new Error('Failed to fetch task count');
    else if(response.status === 204) return []; // Returns empty if no content

    return response.json();
}

// Add new task
export const addTask = async (task) => {
    const user = localStorage.getItem("user");
    const jwt = JSON.parse(user);

    const response = await fetch(BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${jwt.token}`,
        },
        body: JSON.stringify(task),
    });

    if(!response.status === 201) throw new Error('Failed to add task');

    return response.json();
}

// Update task
export const updateTask = async (task) => {
    const user = localStorage.getItem("user");
    const jwt = JSON.parse(user);

    const response = await fetch(`${BASE_URL}/${task.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${jwt.token}`,
        },
        body: JSON.stringify(task),
    });

    if(!response.status === 201) throw new Error('Failed to update task');

    return response.json();
}

// Toggle task completion
export const toggleCompletion = async (task) => {
    const user = localStorage.getItem("user");
    const jwt = JSON.parse(user);

    const response = await fetch(`${BASE_URL}/${task.id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${jwt.token}`,
        },
        body: JSON.stringify({
            isCompleted: !task.isCompleted
        }),
    });

    if(!response.ok) throw new Error('Failed to toggle task completion');

    return response.json();
}

// Delete task
export const deleteTask = async (taskId) => {
    const user = localStorage.getItem("user");
    const jwt = JSON.parse(user);

    const response = await fetch(`${BASE_URL}/${taskId}`,{
        method: 'DELETE',
        headers: {
            "Authorization": `Bearer ${jwt.token}`,
        }  
    })

    if(!response.status === 204) throw new Error('Failed to delete task');

    return taskId;
}
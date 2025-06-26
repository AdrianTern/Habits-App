const BASE_URL = '/api/auth';

// Register user
export const registerUser = async (formData) => {
    const url = `${BASE_URL}/register`;
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData),
    });

    return response;
}

// Login
export const loginUser = async (formData) => {
    const url = `${BASE_URL}/login`;
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData),
    });

    return response;
}

// Change password
export const changePassword = async (id, passwordData) => {
    const url = `${BASE_URL}/changePassword/${id}`; 
    const response = await fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(passwordData),
    });

    return response;
}
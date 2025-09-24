import { fetchWithAuth } from "../utils/fetchWithAuth";

// Functions for authentication related api calls
const BASE_URL = import.meta.env.VITE_API_BASE_URL + '/api/auth';

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

    if(!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message);
    }

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

    if(!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || 'Invalid credentials');
    }

    return response;
}

// Change password
export const changePassword = async (id, passwordData) => {
    const url = `${BASE_URL}/changePassword/${id}`; 

    const response = await fetchWithAuth(url, {
        method: "PATCH",
        body: JSON.stringify(passwordData),
    });

    if(!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message);
    }

    return response;
}
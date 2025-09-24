export const fetchWithAuth = async(url, options = {}) => {
    const user = JSON.parse(localStorage.getItem('user'));
    const token = user?.token;

    const response = await fetch(url, {
        ...options,
        headers: {
            ...(options.headers || {}),
            "Content-Type": "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
    });

    if (response.status === 401) {
        localStorage.removeItem('user');
        window.location.href = "/login";
        return;
    }

    return response;
};
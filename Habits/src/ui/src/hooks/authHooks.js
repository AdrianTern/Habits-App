import { useContext, useEffect } from "react"
import { useLocation } from "react-router-dom";
import { AuthContext } from "../context/AuthContext"
import * as api from '../api/authAPI';
import { toast } from "react-toastify";
import { goToHome, goToLogin } from "../utils/navigation";

export const useAuthState = () => {
    const { user, resError } = useContext(AuthContext);
    return { user, resError };
}

export const useAuth = () => {
    const { login, logout, setResError } = useContext(AuthContext);
    const location = useLocation();

    // Resets state if path changes
    useEffect(() => {
        setResError('');
    }, [location.pathname]);

    const registerUser = async (formData) => {
        try {
            const res = await api.registerUser(formData);
            if (res.status === 201) {
                goToLogin();
                toast.success("User registered. Please login now.");
            }
        } catch (err) {
            console.log(err);
            setResError(err.message);
            console.error("Failed to register user: " + err.message);
        }
    };

    const loginUser = async (formData) => {
        try {
            const res = await api.loginUser(formData);
            if (res.ok) {
                const user = await res.json();
                login(user);
                goToHome();
            }
        } catch (err) {
            setResError(err.message);
            console.error("Failed to login user: " + err.message);
        }
    };

    const logoutUser = () => {
        logout();
        goToLogin();
    };

    const changePassword = async (id, passwordData) => {
        try {
            const res = await api.changePassword(id, passwordData);
            if (res.ok) {
                logout();
                goToLogin();
                toast.info('Password changed. Please login again');
            }
        } catch (err) {
            setResError(err.message);
            console.error("Failed to change password: " + err.message);
        }
    };

    return {
        registerUser,
        loginUser,
        logoutUser,
        changePassword,
    };
};
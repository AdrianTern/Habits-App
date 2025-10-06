import { useContext, useEffect } from "react"
import { useLocation } from "react-router-dom";
import { AuthContext } from "../context/AuthContext"
import * as api from '../api/authAPI';
import { toast } from "react-toastify";
import { goToHome, goToLogin } from "../utils/navigation";

export const useAuthState = () => {
    const {
        user,
        resError,
        registerLoading,
        loginLoading,
        changePassLoading 
    } = useContext(AuthContext);

    return { 
        user,
        resError,
        registerLoading,
        loginLoading,
        changePassLoading  };
}

export const useAuth = () => {
    const { 
        login, 
        logout, 
        setResError, 
        setRegisterLoading, 
        setLoginLoading, 
        setChangePassLoading 
    } = useContext(AuthContext);
    const location = useLocation();

    // Resets state if path changes
    useEffect(() => {
        setResError('');
    }, [location.pathname]);

    const registerUser = async (formData) => {
        setRegisterLoading(true)
        try {
            const res = await api.registerUser(formData);
            if (res.status === 201) {
                goToLogin();
                toast.success("User registered. Please login now.");
            }
        } catch (err) {
            setResError(err.message);
            console.error("Failed to register user: " + err.message);
        } finally {
            setRegisterLoading(false);
        }
    };

    const loginUser = async (formData) => {
        setLoginLoading(true);
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
        } finally {
            setLoginLoading(false);
        }
    };

    const logoutUser = () => {
        logout();
        goToLogin();
    };

    const changePassword = async (id, passwordData) => {
        setChangePassLoading(true);
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
        } finally {
            setChangePassLoading(false);
        }
    };

    return {
        registerUser,
        loginUser,
        logoutUser,
        changePassword,
    };
};
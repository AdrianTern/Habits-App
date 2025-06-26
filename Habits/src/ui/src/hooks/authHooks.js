import { useContext, useEffect } from "react"
import { useLocation, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext"
import * as api from '../api/authAPI';
import { toast } from "react-toastify";

export const useAuthState = () => {
    const { user, errorMsg } = useContext(AuthContext);
    return { user, errorMsg };
}

export const useAuth = () => {
    const { login, logout, setErrorMsg } = useContext(AuthContext);
    const navigate = useNavigate();
    const location = useLocation();

    // Resets state if path changes
    useEffect(() => {
        setErrorMsg('');
    }, [location.pathname]);

    const registerUser = async (formData) => {
        const res = await api.registerUser(formData);
        if (res.status === 201) {
            navigate('/login');
            toast.success("User registered. Please login now.");
        } else {
            const error = await res.json();
            setErrorMsg(error.message);
        }
    }

    const loginUser = async (formData) => {
        const res = await api.loginUser(formData);
        if (res.ok) {
            const user = await res.json();
            login(user);
            navigate('/');
        } else {
            setErrorMsg('Invalid credentials');
        }
    }

    const logoutUser = () => {
        logout();
        navigate('/login');
    }

    const changePassword = async (id, passwordData) => {
        const res = await api.changePassword(id, passwordData);
        if (res.ok) {
            logout();
            navigate('/login');
            toast.info('Password changes. Please login again');
        } else {
            const error = await res.json();
            setErrorMsg(error.message);
        }
    }

    return {
        registerUser,
        loginUser,
        logoutUser,
        changePassword,
    }
}
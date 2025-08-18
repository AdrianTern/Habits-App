import { createContext, useState } from "react";
import { STRINGS } from "../constants/strings";

// Context for user authentication
export const AuthContext = createContext();

export const AuthStateProvider = ({ children }) => {
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem(STRINGS.LOCALSTORAGE_KEY.USER);
        return stored ? JSON.parse(stored) : null;
    });
    
    // State to capture response error
    const [resError, setResError] = useState('');
    
    const login = (userData) => {
        setUser(userData);
        localStorage.setItem(STRINGS.LOCALSTORAGE_KEY.USER, JSON.stringify(userData));
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem(STRINGS.LOCALSTORAGE_KEY.USER);
    };
    return (
       <AuthContext.Provider value={{ user, resError, setResError, login, logout }}>
        {children}
       </AuthContext.Provider> 
    );
};
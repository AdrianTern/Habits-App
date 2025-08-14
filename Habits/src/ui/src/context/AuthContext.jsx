import { createContext, useEffect, useState } from "react";

// Context for user authentication
export const AuthContext = createContext();

export function AuthStateProvider({ children }) {
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem('user');
        return stored ? JSON.parse(stored) : null;
    });
    
    // State to capture response error
    const [resError, setResError] = useState('');
    
    const login = (userData) => {
        setUser(userData);
        localStorage.setItem("user", JSON.stringify(userData));
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem("user");
    };
    return (
       <AuthContext.Provider value={{ user, resError, setResError, login, logout }}>
        {children}
       </AuthContext.Provider> 
    );
};
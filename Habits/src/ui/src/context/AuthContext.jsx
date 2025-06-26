import { createContext, useEffect, useState } from "react";

// Context for user authentication
export const AuthContext = createContext();

export function AuthStateProvider({ children }) {
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem('user');
        return stored ? JSON.parse(stored) : null;
    });
    
    const [errorMsg, setErrorMsg] = useState('');
    
    const login = (userData) => {
        setUser(userData)
        localStorage.setItem("user", JSON.stringify(userData));
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem("user");
    };
    return (
       <AuthContext.Provider value={{ user, errorMsg, setErrorMsg, login, logout }}>
        {children}
       </AuthContext.Provider> 
    );
};
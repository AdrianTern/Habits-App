import { useReducer, useEffect } from "react";

// Custom hook to store/load states from browser local storage
export const usePersistentReducer = ( 
    reducer, 
    defaultState, 
    storageKey ) =>{
    // Retrieve state value from local storage
    const loadState = () => {
        try{
            const serialized = localStorage.getItem(storageKey);
            if(serialized === null) return defaultState;
            return JSON.parse(serialized);
        } catch (e) {
            console.error("Failed to load state", e);
            return defaultState;
        }
    };

    const [state, dispatch] = useReducer(reducer, loadState());

    // Updates state value to local storage
    useEffect(() => {
        try{
            const serialized = JSON.stringify(state);
            localStorage.setItem(storageKey, serialized);
        } catch (e){
            console.error("Failed to save state", e);
        }
    }, [state, storageKey]);

    return [state, dispatch];
};
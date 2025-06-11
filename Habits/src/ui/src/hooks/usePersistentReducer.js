import { useReducer, useEffect } from "react";

export function usePersistentReducer( reducer, defaultState, storageKey ){
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

    useEffect(() => {
        try{
            const serialized = JSON.stringify(state);
            localStorage.setItem(storageKey, serialized);
        } catch (e){
            console.error("Failed to save state", e);
        }
    }, [state, storageKey]);

    return [state, dispatch];
}
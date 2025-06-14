import { useContext } from "react";
import { SettingsContext } from "../context/SettingsContext";

// Custom hook that returns settings state
export function useSettingsState() {
    const { state } = useContext(SettingsContext);
    return state;
}

// Custom hook that provides actions to configure settings states
export function useSettings() {
    const{ dispatch } = useContext(SettingsContext);

    // Show/hide task description
    const handleChangeTaskDescVisibility = (newVal) => {
        dispatch({ type: 'SET_DESC_VISIBILITY', payload: newVal });
    }

    // Show/hide task date
    const handleChangeTaskDateVisibility = (newVal) => {
        dispatch({ type: 'SET_DATE_VISIBILITY', payload: newVal });
    }

    // Toggle light/dark mode
    const handleDarkMode = (newVal) => {
        dispatch({ type: 'SET_DARKMODE', payload: newVal });
    }
    
    return {
        handleChangeTaskDescVisibility,
        handleChangeTaskDateVisibility,
        handleDarkMode,
    };
}
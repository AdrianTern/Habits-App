import { useContext } from "react";
import { SettingsContext } from "../context/settings/SettingsContext";

export function useSettingsState() {
    const { state } = useContext(SettingsContext);
    return state;
}

export function useSettingsActions() {
    const{ dispatch } = useContext(SettingsContext);

    const handleChangeTaskDescVisibility = (newVal) => {
        dispatch({ type: 'SET_DESC_VISIBILITY', payload: newVal });
    }

    const handleChangeTaskDateVisibility = (newVal) => {
        dispatch({ type: 'SET_DATE_VISIBILITY', payload: newVal });
    }

    const handleDarkMode = (newVal) => {
        dispatch({ type: 'SET_DARKMODE', payload: newVal });
    }
    
    return {
        handleChangeTaskDescVisibility,
        handleChangeTaskDateVisibility,
        handleDarkMode,
    };
}
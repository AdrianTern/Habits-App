import { createContext } from "react";
import { settingsReducer } from "../reducers/settingsReducer";
import { usePersistentReducer } from "../hooks/usePersistentReducer";

// Context for app settings
export const SettingsContext = createContext();

const initialState = {
  isShowTaskDesc: true,
  isShowTaskDate: true,
  darkMode: false,
};

// Provider component that provides action to configure settings state
export function SettingsStateProvider({ children }) {
  // Custom hook to store/load settings state from localStorage in browser
  const [state, dispatch] = usePersistentReducer(settingsReducer, initialState, 'appSettings');

  return (
    <SettingsContext.Provider value={{state, dispatch}}>
      {children}
    </SettingsContext.Provider>
  );
}

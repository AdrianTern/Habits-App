import { createContext } from "react";
import { settingsReducer } from "../reducers/settingsReducer";
import { usePersistentReducer } from "../hooks/usePersistentReducer";
import { STRINGS } from "../constants/strings";

// Context for app settings
export const SettingsContext = createContext();

const initialState = {
  isShowTaskDesc: true,
  isShowTaskDate: true,
  darkMode: false,
};

// Provider component that provides action to configure settings state
export const SettingsStateProvider = ({ children }) => {
  // Custom hook to store/load settings state from localStorage in browser
  const [state, dispatch] = usePersistentReducer(settingsReducer, initialState, STRINGS.LOCALSTORAGE_KEY.SETTINGS);

  return (
    <SettingsContext.Provider value={{state, dispatch}}>
      {children}
    </SettingsContext.Provider>
  );
};

import { createContext } from "react";
import { settingsReducer } from "./settingsReducer";
import { usePersistentReducer } from "../../hooks/usePersistentReducer";

export const SettingsContext = createContext();

const initialState = {
  isShowTaskDesc: true,
  isShowTaskDate: true,
  darkMode: false,
};

export function SettingsStateProvider({ children }) {
  const [state, dispatch] = usePersistentReducer(settingsReducer, initialState, 'appSettings');

  return (
    <SettingsContext.Provider value={{state, dispatch}}>
      {children}
    </SettingsContext.Provider>
  );
}

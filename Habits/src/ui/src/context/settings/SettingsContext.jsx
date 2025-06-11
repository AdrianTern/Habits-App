import { createContext } from "react";
import { settingsReducer } from "./settingsReducer";
import { useReducer } from "react";

export const SettingsContext = createContext();

const initialState = {
  isShowTaskDesc: true,
  isShowTaskDate: true,
  darkMode: false,
  openTaskForm: false,
};

export function SettingsStateProvider({ children }) {
  const [state, dispatch] = useReducer(settingsReducer, initialState);

  return (
    <SettingsContext.Provider value={{state, dispatch}}>
      {children}
    </SettingsContext.Provider>
  );
}

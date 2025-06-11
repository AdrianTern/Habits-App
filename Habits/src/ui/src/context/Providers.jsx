import { SettingsStateProvider } from "./settings/SettingsContext";
import { TaskStateProvider } from "./tasks/TaskContext";
import { useSettingsState } from '../hooks/settingsHooks';
import { ThemeWrapper } from "../wrapper/ThemeWrapper";

export function Providers({ children }) {
    return (
        <SettingsStateProvider>
            <ThemeWrapper>
                <TaskStateProvider>
                    {children}
                </TaskStateProvider>
            </ThemeWrapper>
        </SettingsStateProvider>
    )
}
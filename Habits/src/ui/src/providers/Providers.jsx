import { SettingsStateProvider } from "../context/SettingsContext";
import { TaskStateProvider } from "../context/TaskViewContext";
import { ThemeWrapper } from "./ThemeWrapper";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

// Initialize QueryClient to allow react-query
const queryClient = new QueryClient();

// Component to combine all provider components
export function Providers({ children }) {
    return (
        <QueryClientProvider client={queryClient}>
            <SettingsStateProvider>
                <ThemeWrapper>
                    <TaskStateProvider>
                        {children}
                    </TaskStateProvider>
                </ThemeWrapper>
            </SettingsStateProvider>
        </QueryClientProvider>
    )
}
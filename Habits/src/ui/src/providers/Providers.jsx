import { SettingsStateProvider } from "../context/SettingsContext";
import { TaskStateProvider } from "../context/TaskViewContext";
import { ThemeWrapper } from "./ThemeWrapper";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthStateProvider } from "../context/AuthContext";

// Initialize QueryClient to allow react-query
const queryClient = new QueryClient();

// Component to combine all provider components
export const Providers = ({ children }) => {
    return (
        <QueryClientProvider client={queryClient}>
            <AuthStateProvider>
                <SettingsStateProvider>
                    <ThemeWrapper>
                        <TaskStateProvider>
                            {children}
                        </TaskStateProvider>
                    </ThemeWrapper>
                </SettingsStateProvider>
            </AuthStateProvider>
        </QueryClientProvider>
    )
};
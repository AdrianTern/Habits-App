import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { useSettingsState } from '../hooks/settingsHooks';
import { AnimatePresence } from 'framer-motion';
import { lightTheme, darkTheme } from '../styles/theme';

// A provider for themes related components
export function ThemeWrapper({ children }) {
    const settingsState = useSettingsState();

    return (
        <ThemeProvider theme={settingsState.darkMode ? darkTheme : lightTheme}>
            <CssBaseline />
            <AnimatePresence>
                {children}
            </AnimatePresence>
        </ThemeProvider>
    )
}
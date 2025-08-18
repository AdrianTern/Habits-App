import { Checkbox } from '@mui/material';
import LightModeRoundedIcon from '@mui/icons-material/LightModeRounded';
import NightsStayRoundedIcon from '@mui/icons-material/NightsStayRounded';
import { styled } from '@mui/material/styles';
import { useSettings, useSettingsState } from '../hooks/settingsHooks';

// Styled checkbox to toggle light/dark mode
const ThemeModeCheckBox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.custom.darkmode,
    '&.Mui-checked': {
        color: theme.palette.custom.lightmode,
    },
    transition: 'transform 0.3s ease-in-out',
    '&:hover': {
        transform: 'rotate(360deg)'
    },

}));

const ThemeSwitch = () => {
    const state = useSettingsState();
    const { handleDarkMode } = useSettings();

    return (
        <ThemeModeCheckBox
            aria-label='toggle light/dark mode'
            size='medium'
            checked={state.darkMode}
            onChange={() => handleDarkMode(!state.darkMode)}
            icon={<NightsStayRoundedIcon />}
            checkedIcon={<LightModeRoundedIcon />}
        />
    )
};

export default ThemeSwitch;
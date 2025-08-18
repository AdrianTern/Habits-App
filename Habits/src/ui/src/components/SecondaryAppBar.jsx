import { AppBar, Toolbar, Box } from '@mui/material';
import HomeButton from './HomeButton';
import ThemeSwitch from './ThemeSwitch';

const SecondaryAppBar = () => {
    return (
        <AppBar position='fixed' sx={{ backgroundColor: 'custom.black' }}>
            <Toolbar sx={{ display: 'flex', position: 'relative' }}>
                <Box sx={{ position: 'absolute', left: '50%', transform: 'translateX(-50%)' }}>
                    <HomeButton />
                </Box>
                <Box sx={{ ml: 'auto' }}>
                    <ThemeSwitch />
                </Box>
            </Toolbar>
        </AppBar>
    )
};

export default SecondaryAppBar;
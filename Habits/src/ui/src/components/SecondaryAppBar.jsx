import { AppBar, Toolbar } from '@mui/material';
import HomeButton from './HomeButton';

function SecondaryAppBar() {
    // Hard-coded string
    const title = "{habits.}"
    
    return (
        <>
            <AppBar position='fixed' sx={{ backgroundColor: 'custom.black' }}>
                <Toolbar>
                    <HomeButton />
                </Toolbar>
            </AppBar>
        </>
    )
}

export default SecondaryAppBar;
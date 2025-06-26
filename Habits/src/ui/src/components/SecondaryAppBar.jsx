import { AppBar, Toolbar, Typography} from '@mui/material';

function SecondaryAppBar() {
    // Hard-coded string
    const title = "{habits.}"
    
    return (
        <>
            <AppBar position='fixed' sx={{ backgroundColor: 'custom.black' }}>
                <Toolbar>
                    <Typography variant='h6' sx={{ flexGrow: 1, letterSpacing: 1 }}>
                        {title}
                    </Typography>
                </Toolbar>
            </AppBar>
        </>
    )
}

export default SecondaryAppBar;
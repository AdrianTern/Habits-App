import {
    Checkbox,
    AppBar,
    Toolbar,
    Typography,
    IconButton,
    Menu,
    MenuItem,
    FormControlLabel,
} from '@mui/material';
import TuneRoundedIcon from '@mui/icons-material/TuneRounded';
import VisibilityRoundedIcon from '@mui/icons-material/VisibilityRounded';
import VisibilityOffRoundedIcon from '@mui/icons-material/VisibilityOffRounded';
import LightModeRoundedIcon from '@mui/icons-material/LightModeRounded';
import NightsStayRoundedIcon from '@mui/icons-material/NightsStayRounded';
import { useState } from 'react';
import { styled } from '@mui/material/styles';

const MenuCheckBox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.custom.white,
    '&.Mui-checked': {
        color: theme.palette.custom.darkred,
    }
}));

const ThemeCheckBox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.custom.darkmode,
    '&.Mui-checked': {
        color: theme.palette.custom.lightmode,
    },
    transition: 'transform 0.3s ease-in-out',
    '&:hover': {
        transform: 'rotate(360deg)'
    },

}));

function MainAppBar({ onChange, appSettings }) {
    const title = "{habits.}"
    const [anchorEl, setAnchorEl] = useState(null);
    const IS_SHOW_DESC = 'isShowTaskDesc';
    const IS_SHOW_DATE = 'isShowTaskDate';
    const DARK_MODE = 'darkMode';

    const openMenu = (event) => {
        setAnchorEl(event.currentTarget);
    }

    const closeMenu = () => {
        setAnchorEl(null);
    }

    const handleOnChange = (name) => () => {
        onChange({
            ...appSettings,
            [name]: !appSettings[name],
        })
    }

    return (
        <>
            <AppBar
                position='static'
                sx={{
                    backgroundColor: 'custom.black'
                }}
            >
                <Toolbar>
                    <IconButton
                        size='medium'
                        edge='start'
                        aria-label='menu'
                        onClick={openMenu}
                        sx={{
                            color: 'white',
                            mr: 2
                        }}
                    >
                        <TuneRoundedIcon />
                    </IconButton>
                    <Typography
                        variant='h6'
                        component='div'
                        sx={{
                            flexGrow: 1,
                            letterSpacing: 1,
                        }}>
                        {title}
                    </Typography>
                    <ThemeCheckBox 
                        size='large'
                        key={DARK_MODE}
                        checked={appSettings.darkMode}
                        onChange={handleOnChange(DARK_MODE)}
                        icon={<NightsStayRoundedIcon />}
                        checkedIcon={<LightModeRoundedIcon />}
                    />
                </Toolbar>
            </AppBar>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={closeMenu}
                slotProps={{
                    paper: {
                        sx: {
                            backgroundColor: 'custom.black',
                            color: 'custom.white'
                        }
                    }
                }}
            >
                <MenuItem>
                    <FormControlLabel
                        control={
                            <MenuCheckBox
                                key={IS_SHOW_DESC}
                                checked={!appSettings.isShowTaskDesc}
                                onChange={handleOnChange(IS_SHOW_DESC)}
                                icon={<VisibilityRoundedIcon/>}
                                checkedIcon={<VisibilityOffRoundedIcon />}
                            />
                        }
                        label="Show description"
                    />
                </MenuItem>
                <MenuItem>
                    <FormControlLabel
                        control={
                            <MenuCheckBox
                                key={IS_SHOW_DATE}
                                checked={!appSettings.isShowTaskDate}
                                onChange={handleOnChange(IS_SHOW_DATE)}
                                icon={<VisibilityRoundedIcon />}
                                checkedIcon={<VisibilityOffRoundedIcon />}
                            />
                        }
                        label="Show due date"
                    />
                </MenuItem>
            </Menu>
        </>
    )
}

export default MainAppBar;
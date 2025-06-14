import {
    Checkbox,
    AppBar,
    Toolbar,
    Typography,
    IconButton,
    Drawer,
    Box,
    List,
    ListSubheader,
    ListItemButton,
    ListItemIcon,
    ListItemText,
} from '@mui/material';
import MenuRoundedIcon from '@mui/icons-material/MenuRounded';
import VisibilityRoundedIcon from '@mui/icons-material/VisibilityRounded';
import VisibilityOffRoundedIcon from '@mui/icons-material/VisibilityOffRounded';
import LightModeRoundedIcon from '@mui/icons-material/LightModeRounded';
import NightsStayRoundedIcon from '@mui/icons-material/NightsStayRounded';
import ArrowRight from '@mui/icons-material/ArrowRight';
import { styled } from '@mui/material/styles';
import { useSettingsState, useSettings } from '../hooks/settingsHooks';
import { useState } from 'react';

// Styled checkbox for visibility settings menu
const VisibilityCheckBox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.custom.white,
    '&.Mui-checked': {
        color: theme.palette.custom.darkred,
    }
}));

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

// Styled button to open side drawer
const MenuButton = styled(IconButton)(({ theme }) => ({
    color: theme.palette.custom.white,
    '& svg': {
        transition: '0.2s',
    },
    '&:hover': {
        '& svg:first-of-type': {
            transform: 'translateX(-8px)',
        },
        '& svg:last-of-type': {
            right: 0,
            opacity: 1,
        }
    },
}))

// Styled drawer to indicate side drawer
const SideDrawer = styled(Drawer)(({ theme }) => ({
    '& .MuiDrawer-paper': {
        backgroundColor: theme.palette.custom.black,
        color: theme.palette.custom.white,
    },
}));

// Styled container for the drawer
const DrawerBox = styled(Box)(({ theme }) => ({
    width: '50vw',

    [theme.breakpoints.up('sm')]: {
        width: '30vw',
    },

    [theme.breakpoints.up('lg')]: {
        width: '20vw',
    },

}));

// Styled container for footer in drawer
const FooterBox = styled(Box)({
    position: 'absolute',
    bottom: 10,
    left: 10,
    width: '90%',
})

function MainAppBar() {
    // State to toggle drawer
    const [isDrawerOpen, setIsDrawerOpen] = useState(false);

    // Get settings states and relevant actions
    const state = useSettingsState();
    const { handleChangeTaskDescVisibility, handleChangeTaskDateVisibility, handleDarkMode } = useSettings();

    // Hard-coded string
    const IS_SHOW_DESC = 'isShowTaskDesc';
    const IS_SHOW_DATE = 'isShowTaskDate';
    const title = "{habits.}"

    // Handler to open/close drawer
    const toggleDrawer = (newVal) => () => {
        setIsDrawerOpen(newVal);
    }

    // Handler to show/hide task description
    const handleToggleTaskDesc = () => {
        handleChangeTaskDescVisibility(!state.isShowTaskDesc);
    }

    // Handler to show/hide task date
    const handleToggleTaskDate = () => {
        handleChangeTaskDateVisibility(!state.isShowTaskDate);
    }

    // Returns header component in the side drawer
    const DrawerHeader = () => {
        const title = '{h.}'
        return (
            <Box ml={1} mt={1}>
                <Typography variant='h4' gutterBottom color='custom.violet' letterSpacing={1} >
                    {title}
                </Typography>
            </Box>
        );
    }

    // Returns body component in the side drawer
    const DrawerBody = () => {
        return (
            <Box>
                <List dense subheader={<ListSubheader sx={{ backgroundColor: 'inherit', color: 'custom.lightgrey' }}>Task visibility</ListSubheader>} >
                    <ListItemButton key={IS_SHOW_DESC} onClick={handleToggleTaskDesc} >
                        <ListItemIcon>
                            <VisibilityCheckBox
                                aria-label='show/hide task description'
                                checked={!state.isShowTaskDesc}
                                onChange={handleToggleTaskDesc}
                                icon={<VisibilityRoundedIcon />}
                                checkedIcon={<VisibilityOffRoundedIcon />}
                            />
                        </ListItemIcon>
                        <ListItemText>
                            <Typography variant='body2'> Show description</Typography>
                        </ListItemText>
                    </ListItemButton>
                    <ListItemButton key={IS_SHOW_DATE} onClick={handleToggleTaskDate}>
                        <ListItemIcon>
                            <VisibilityCheckBox
                                aria-label='show/hide task date'
                                checked={!state.isShowTaskDate}
                                onChange={handleToggleTaskDate}
                                icon={<VisibilityRoundedIcon />}
                                checkedIcon={<VisibilityOffRoundedIcon />}
                            />
                        </ListItemIcon>
                        <ListItemText>
                            <Typography variant='body2'> Show date</Typography>
                        </ListItemText>
                    </ListItemButton>
                </List>
            </Box>
        );
    }
    
    // Returns footer component in the side drawer
    const DrawerFooter = () => {
        const footerText = '© {habits.} developed by AdrianTern';
        return (
            <FooterBox>
                <Typography variant='caption' gutterBottom color='custom.lightgrey'>
                    {footerText}
                </Typography>
            </FooterBox>
        );
    }

    // Components in the side drawer
    const DrawerComps = (
        <DrawerBox role="presentation">
            <DrawerHeader />
            <DrawerBody />
            <DrawerFooter />
        </DrawerBox>
    )

    return (
        <>
            <AppBar position='fixed' sx={{ backgroundColor: 'custom.black' }}>
                <Toolbar>
                    <MenuButton
                        size='medium'
                        edge='start'
                        aria-label='app menu'
                        onClick={toggleDrawer(true)}
                    >
                        <MenuRoundedIcon />
                        <ArrowRight sx={{ position: 'absolute', right: 4, opacity: 0 }} />
                    </MenuButton>
                    <Typography variant='h6' sx={{ flexGrow: 1, letterSpacing: 1 }}>
                        {title}
                    </Typography>
                    <ThemeModeCheckBox
                        aria-label='toggle light/dark mode'
                        size='large'
                        checked={state.darkMode}
                        onChange={() => handleDarkMode(!state.darkMode)}
                        icon={<NightsStayRoundedIcon />}
                        checkedIcon={<LightModeRoundedIcon />}
                    />
                </Toolbar>
            </AppBar>
            <SideDrawer open={isDrawerOpen} onClose={toggleDrawer(false)} >
                {DrawerComps}
            </SideDrawer>
        </>
    )
}

export default MainAppBar;
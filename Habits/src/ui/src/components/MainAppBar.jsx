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
import { useSettingsState, useSettingsActions } from '../hooks/settingsHooks';
import { useState } from 'react';

const VisibilityCheckBox = styled(Checkbox)(({ theme }) => ({
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

const MainDrawer = styled(Drawer)(({ theme }) => ({
    '& .MuiDrawer-paper': {
        backgroundColor: theme.palette.custom.black,
        color: theme.palette.custom.white,
    },
}));

const DrawerBox = styled(Box)(({ theme }) => ({
    width: '50vw',

    [theme.breakpoints.up('sm')]: {
        width: '30vw',
    },

    [theme.breakpoints.up('lg')]: {
        width: '20vw',
    },

}));

const Footer = styled(Box)({
    position: 'absolute',
    bottom: 10,
    left: 10,
    width: '90%',
})

function MainAppBar() {
    const title = "{habits.}"
    const [isDrawerOpen, setIsDrawerOpen] = useState(false);

    const state = useSettingsState();
    const { handleChangeTaskDescVisibility, handleChangeTaskDateVisibility, handleDarkMode } = useSettingsActions();

    const IS_SHOW_DESC = 'isShowTaskDesc';
    const IS_SHOW_DATE = 'isShowTaskDate';
    const DARK_MODE = 'darkMode';

    const toggleDrawer = (newVal) => () => {
        setIsDrawerOpen(newVal);
    }

    const handleToggleTaskDesc = () => {
        handleChangeTaskDescVisibility(!state.isShowTaskDesc);
    }

    const handleToggleTaskDate = () => {
        handleChangeTaskDateVisibility(!state.isShowTaskDate);
    }

    const MenuHeader = () => {
        const menuTitle = '{h.}'
        return (
            <Box ml={1} mt={1}>
                <Typography variant='h4' gutterBottom color='custom.violet' letterSpacing={1} >
                    {menuTitle}
                </Typography>
            </Box>
        );
    }

    const MenuFooter = () => {
        const footerText = "© {habits.} developed by AdrianTern"
        return (
            <Footer>
                <Typography variant='caption' gutterBottom color='custom.lightgrey'>
                    {footerText}
                </Typography>
            </Footer>
        );
    }

    const MenuList = () => {
        return (
            <Box>
                <List dense subheader={<ListSubheader sx={{ backgroundColor: 'inherit', color: 'custom.lightgrey' }}>Task visibility</ListSubheader>} >
                    <ListItemButton key={IS_SHOW_DESC} onClick={handleToggleTaskDesc} >
                        <ListItemIcon>
                            <VisibilityCheckBox
                                key={IS_SHOW_DESC}
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
                                key={IS_SHOW_DATE}
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
    const DrawerList = (
        <DrawerBox role="presentation">
            <MenuHeader />
            <MenuList />
            <MenuFooter />
        </DrawerBox>
    )

    return (
        <>
            <AppBar
                position='fixed'
                sx={{
                    backgroundColor: 'custom.black'
                }}
            >
                <Toolbar>
                    <MenuButton
                        size='medium'
                        edge='start'
                        aria-label='menu'
                        onClick={toggleDrawer(true)}
                    >
                        <MenuRoundedIcon />
                        <ArrowRight sx={{ position: 'absolute', right: 4, opacity: 0 }} />
                    </MenuButton>
                    <Typography variant='h6' sx={{ flexGrow: 1, letterSpacing: 1 }}>
                        {title}
                    </Typography>
                    <ThemeCheckBox
                        size='large'
                        key={DARK_MODE}
                        checked={state.darkMode}
                        onChange={() => handleDarkMode(!state.darkMode)}
                        icon={<NightsStayRoundedIcon />}
                        checkedIcon={<LightModeRoundedIcon />}
                    />
                </Toolbar>
            </AppBar>
            <MainDrawer open={isDrawerOpen} onClose={toggleDrawer(false)} >
                {DrawerList}
            </MainDrawer>
        </>
    )
}

export default MainAppBar;
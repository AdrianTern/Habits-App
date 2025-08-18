import {
    AppBar,
    Toolbar,
    IconButton,
    Box,
    Menu,
    MenuItem,
} from '@mui/material';
import MenuRoundedIcon from '@mui/icons-material/MenuRounded';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ArrowRight from '@mui/icons-material/ArrowRight';
import ChangeCircleRoundedIcon from '@mui/icons-material/ChangeCircleRounded';
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';
import { styled } from '@mui/material/styles';
import { useState } from 'react';
import { useAuth, useAuthState } from '../hooks/authHooks';
import HomeButton from './HomeButton';
import ThemeSwitch from './ThemeSwitch';
import { goToChangePassword } from '../utils/navigation';
import MenuDrawer from './MenuDrawer';

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

const MainAppBar = () => {
    // State to toggle drawer
    const [isDrawerOpen, setIsDrawerOpen] = useState(false);

    // State to open user menu
    const [userMenuAnchorEl, setUserMenuAnchorEl] = useState(null);
    const open = Boolean(userMenuAnchorEl);

    // Get user state and auth actions
    const { user } = useAuthState();
    const { logoutUser } = useAuth();

    // Handler to open user menu
    const handleOpenUserMenu = (event) => {
        setUserMenuAnchorEl(event.currentTarget);
    };

    // Handler to close user menu
    const handleCloseUserMenu = () => {
        setUserMenuAnchorEl(null);
    };

    // Handler to open/close drawer
    const toggleDrawer = (newVal) => () => {
        setIsDrawerOpen(newVal);
    };

    // Navigate to change password page
    const handleChangePassword = () => {
        goToChangePassword();
    };

    // User menu
    const UserMenu = () => (
        <Menu
            anchorEl={userMenuAnchorEl}
            open={open}
            onClose={handleCloseUserMenu}
            slotProps={{
                paper: {
                    sx: {
                        backgroundColor: "custom.black",
                        color: 'custom.white',
                        borderRadius: '5px'
                    },
                },
            }}
        >
            <MenuItem disabled><AccountCircleIcon sx={{ pr: '5px' }} />{user?.username ?? "Guest"}</MenuItem>
            <MenuItem onClick={handleChangePassword}><ChangeCircleRoundedIcon sx={{ pr: '5px' }} /> Change password</MenuItem>
            <MenuItem onClick={logoutUser}><LogoutRoundedIcon sx={{ pr: '5px' }} /> Logout</MenuItem>
        </Menu>
    );

    return (
        <>
            <AppBar position='fixed' sx={{ backgroundColor: 'custom.black' }}>
                <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    {/* App Menu */}
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <MenuButton
                            size='medium'
                            edge='start'
                            aria-label='app menu'
                            onClick={toggleDrawer(true)}
                        >
                            <MenuRoundedIcon />
                            <ArrowRight sx={{ position: 'absolute', right: 4, opacity: 0 }} />
                        </MenuButton>
                    </Box>

                    {/* Home Button */}
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <HomeButton />
                    </Box>

                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        {/* Theme Switch */}
                        <ThemeSwitch />

                        {/* User Menu Button*/}
                        <IconButton size='medium' sx={{ color: 'white' }} onClick={handleOpenUserMenu}>
                            <AccountCircleIcon />
                        </IconButton>
                    </Box>
                </Toolbar>
            </AppBar>

            {/* Side Drawer */}
            <MenuDrawer open={isDrawerOpen} onClose={toggleDrawer(false)} />

            {/* User Menu */}
            <UserMenu />
        </>
    )
};

export default MainAppBar;
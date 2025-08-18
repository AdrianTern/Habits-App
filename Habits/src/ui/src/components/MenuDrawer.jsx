import {
    Typography,
    Drawer,
    Box,
    List,
    ListSubheader,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Checkbox,
} from '@mui/material';
import VisibilityRoundedIcon from '@mui/icons-material/VisibilityRounded';
import VisibilityOffRoundedIcon from '@mui/icons-material/VisibilityOffRounded';
import { styled } from '@mui/material/styles';
import { useSettingsState, useSettings } from '../hooks/settingsHooks';
import { STRINGS } from '../constants';

// Styled checkbox for visibility settings menu
const VisibilityCheckBox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.custom.white,
    '&.Mui-checked': {
        color: theme.palette.custom.darkred,
    }
}));

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
const DrawerFooterBox = styled(Box)({
    position: 'absolute',
    bottom: 10,
    left: 10,
    width: '90%',
})

const MenuDrawer = ({
    open,
    onClose,
}) => {
    // Get settings states and relevant actions
    const state = useSettingsState();
    const { handleChangeTaskDescVisibility, handleChangeTaskDateVisibility } = useSettings();

    // Handler to show/hide task description
    const handleToggleTaskDesc = () => {
        handleChangeTaskDescVisibility(!state.isShowTaskDesc);
    };

    // Handler to show/hide task date
    const handleToggleTaskDate = () => {
        handleChangeTaskDateVisibility(!state.isShowTaskDate);
    };

    // Returns header component in the side drawer
    const DrawerHeader = () => {
        return (
            <Box ml={1} mt={1}>
                <Typography variant='h4' gutterBottom color='custom.violet' letterSpacing={1} >
                    {STRINGS.APP_NAME.SHORT}
                </Typography>
            </Box>
        );
    };

    // Returns body component in the side drawer
    const DrawerBody = () => {
        return (
            <Box>
                <List dense subheader={<ListSubheader sx={{ backgroundColor: 'inherit', color: 'custom.lightgrey' }}>Task visibility</ListSubheader>} >
                    {/* Show/Hide Task Description */}
                    <ListItemButton key="isShowTaskDesc" onClick={handleToggleTaskDesc} >
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

                    {/* Show/Hide Task Date */}
                    <ListItemButton key="isShowTaskDate" onClick={handleToggleTaskDate}>
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
    };

    // Returns footer component in the side drawer
    const DrawerFooter = () => {
        return (
            <DrawerFooterBox>
                <Typography variant='caption' gutterBottom color='custom.lightgrey'>
                    {STRINGS.FOOTER_TEXT}
                </Typography>
            </DrawerFooterBox>
        );
    };

    return (
        <SideDrawer open={open} onClose={onClose}>
            <DrawerBox role="presentation">
                <DrawerHeader />
                <DrawerBody />
                <DrawerFooter />
            </DrawerBox>
        </SideDrawer>
    );
};

export default MenuDrawer;
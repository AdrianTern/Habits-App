import ErrorRoundedIcon from '@mui/icons-material/ErrorRounded';
import { Box, Typography } from '@mui/material';

const ErrorMsg = ({ errorMsg }) => {
    return (
        <Box display='flex'>
            <Typography><ErrorRoundedIcon fontSize='small' sx={{ color: 'custom.darkred', pr: 0.5 }} /></Typography>
            <Typography variant='caption' sx={{ color: 'custom.darkred' }}>{errorMsg}</Typography>
        </Box>
    )
};

export default ErrorMsg;
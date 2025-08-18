import { Button, Typography } from '@mui/material';
import { STRINGS } from '../constants';
import { goToHome } from "../utils/navigation";

const HomeButton = () => {
    return (
        <Button
            disableRipple
            disableElevation
            aria-label='go to home page'
            sx={{
                all: 'unset',
                cursor: 'pointer'
            }}
            onClick={() => goToHome()}
        >
            <Typography variant='h6' letterSpacing={1}>{STRINGS.APP_NAME.FULL}</Typography>
        </Button>
    )
};

export default HomeButton;
import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

function HomeButton() {
    const navigate = useNavigate();
    const title = "{habits.}"

    const goToHome = () => {
        navigate('/');
    }

    return (
        <Button
            disableRipple
            disableElevation
            sx={{
                all: 'unset',
                cursor: 'pointer',

            }}
            onClick={goToHome}
        >
            {title}
        </Button>
    )
}

export default HomeButton;
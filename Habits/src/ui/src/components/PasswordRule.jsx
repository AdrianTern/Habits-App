import { Typography } from "@mui/material";
import { CenterBox } from "../styles/StyledComponents";
import { STRINGS } from "../constants";

const PasswordRule = ({ violatedIndex }) => {
    return (
        <>
        <Typography variant="body2" gutterBottom={false} sx={{marginBottom: -3}} color="custom.grey"> Password must have:</Typography>
        <CenterBox>      
            <ul>
                {STRINGS.PASSWORD_RULES.map((rule, index) => (
                    <Typography
                        key={index}
                        component='li'
                        variant="caption"
                        color={violatedIndex.includes(index) ? 'custom.darkred' : 'custom.grey'}
                    >
                        {rule}
                    </Typography>
                ))}
            </ul>
        </CenterBox>
        </>
    );
};

export default PasswordRule;
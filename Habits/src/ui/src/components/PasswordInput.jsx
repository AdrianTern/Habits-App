import { useState } from "react";
import { InputField } from "../styles/StyledComponents";
import { IconButton, InputAdornment } from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";

const PasswordInput = ({
    error,
    label,
    ariaLabel,
    inputRef,
    field
}) => {
    const [showPassword, setShowPassword] = useState(false);

    const handleShowPassword = () => {
        setShowPassword((prev) => !prev);
    }

    return (
        <InputField
            {...field}
            required
            error={error}
            type={showPassword ? 'text' : 'password'}
            label={label}
            aria-label={ariaLabel}
            inputRef={inputRef}
            slotProps={{
                input: {
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton
                                aria-label={showPassword ? 'hide password' : 'show password'}
                                onClick={handleShowPassword}
                                edge="end"
                            >
                                {showPassword ?
                                    <VisibilityOff sx={{ color: 'primary.main' }} /> :
                                    <Visibility sx={{ color: 'primary.main' }} />}
                            </IconButton>
                        </InputAdornment>
                    )
                }
            }}
        />
    );
};

export default PasswordInput;
import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../../hooks/authHooks';
import { useEffect, useRef, useState } from 'react';
import { Typography, Link as MuiLink } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { CenterBox, PrimaryButton, InputField, InputBox } from '../../styles/StyledComponents';
import ErrorMsg from '../ErrorMsg';
import { NUMBERS, ROUTES, STRINGS } from '../../constants';

const RegisterForm = () => {
    // Get function to register user from useAuth hook
    const { registerUser } = useAuth();

    // State to indicate if the registeration failed
    const { resError } = useAuthState();
    // State to indicate invalid password length
    const [passError, setPassError] = useState('');

    // useForm hook to manage state of form inputs
    const { handleSubmit, control }
        = useForm();

    const usernameRef = useRef(null);

    useEffect(() => {
        if (usernameRef.current) usernameRef.current.focus();
    }, []);

    const handleOnSubmit = async (data) => {
        if (data?.password.length >= NUMBERS.MAX_PASSWORD_LENGTH) {
            setPassError('');
            registerUser(data);
        } else {
            setPassError(STRINGS.INVALID_PASSWORD_LENGTH);
        }
    };

    return (
        <InputBox component='form' onSubmit={handleSubmit(handleOnSubmit)} gap={2}>
            {/* Username Input */}
            <Controller
                name='username'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField
                        required
                        error={resError}
                        label='Username'
                        aria-label='enter username'
                        inputRef={usernameRef}
                        {...field}
                    />
                )}
            />

            {/* Password Input */}
            <Controller
                name='password'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField
                        required
                        type='password'
                        error={passError}
                        label='Password'
                        aria-label='enter password'
                        {...field}
                    />
                )}
            />

            {/* Error Message */}
            {(resError || passError) && (
                <ErrorMsg errorMsg={resError ? resError : passError} />
            )}

            {/* Register Button */}
            <PrimaryButton
                type='submit'
                aria-label='register user'
                size='large'
            >
                Register
            </PrimaryButton>

            <CenterBox>
                <Typography variant='subtitle'>
                    Back to {" "}
                    <MuiLink component={RouterLink} to={ROUTES.LOGIN} sx={{ color: 'custom.violet' }}>
                        login
                    </MuiLink>
                </Typography>
            </CenterBox>
        </InputBox>
    )
};

export default RegisterForm;
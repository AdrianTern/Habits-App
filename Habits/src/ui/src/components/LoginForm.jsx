import { useForm, Controller } from 'react-hook-form';
import { styled } from '@mui/material/styles';
import { useAuth, useAuthState } from '../hooks/authHooks';
import { Box, Typography, Link as MuiLink } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import { CenterBox, InputField, PrimaryButton } from '../styles/StyledComponents';
import ErrorMsg from './ErrorMsg';

// Initialize a center box here in order for form component to work
const InputBox = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center,'
});

const LoginForm = () => {
    // Get function to login user from useAuth hook
    const { loginUser } = useAuth();

    // State to indicate if the login failed
    const { resError } = useAuthState();

    // useForm hook to manage state of form inputs
    const { handleSubmit, control }
        = useForm();

    // Focus on username on mount
    const usernameRef = useRef(null);
    useEffect(() => {
        if (usernameRef.current) usernameRef.current.focus();
    }, []);

    // Handler method for submit
    const handleOnSubmit = async (data) => {
        loginUser(data);
    }

    return (
        <InputBox component='form' onSubmit={handleSubmit(handleOnSubmit)} gap={2}>
            <Controller
                name='username'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField error={resError} required label='Username' inputRef={usernameRef} {...field} />
                )}
            />
            <Controller
                name='password'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField error={resError} required type='password' label='Password' {...field} />
                )}
            />
            <ErrorMsg hasError={resError} errorMsg={resError}/>
            <PrimaryButton
                type='submit'
                aria-label='login'
                size='large'
            >
                Continue
            </PrimaryButton>
            <CenterBox>
                <Typography variant='subtitle'>
                    Not a user yet? {" "}
                    <MuiLink component={RouterLink} to='/register' sx={{ color: 'custom.violet' }}>
                        Register
                    </MuiLink>
                </Typography>
            </CenterBox>
        </InputBox>
    )
};

export default LoginForm;
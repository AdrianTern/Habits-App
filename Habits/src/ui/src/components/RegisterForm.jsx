import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../hooks/authHooks';
import { useEffect, useRef, useState } from 'react';
import { styled } from '@mui/material/styles';
import { Box, Typography, Link as MuiLink } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { CenterBox, PrimaryButton, InputField } from '../styles/StyledComponents';
import ErrorMsg from './ErrorMsg';

// Initialize a center box here in order for form component to work
const InputBox = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center,'
});

const RegisterForm = () => {
    // Get function to register user from useAuth hook
    const { registerUser } = useAuth();

    // State to indicate if the registeration failed
    const { resError } = useAuthState();
    // State to indicate invalid password length
    const [passError, setPassError] = useState('');
    const hasError = resError || passError;

    // useForm hook to manage state of form inputs
    const { handleSubmit, control }
        = useForm();

    const usernameRef = useRef(null);

    useEffect(() => {
        if (usernameRef.current) usernameRef.current.focus();
    }, []);

    const handleOnSubmit = async (data) => {
        if( data?.password.length >= 8) {
            setPassError('');
            registerUser(data);
        } else {
            setPassError("Password must have at least 8 characters");
        }  
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
                    <InputField type='password' error={passError} required label='Password' {...field} />
                )}
            />
            <ErrorMsg hasError={hasError} errorMsg={resError ? resError : passError}/>
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
                    <MuiLink component={RouterLink} to='/login' sx={{ color: 'custom.violet' }}>
                        login
                    </MuiLink>
                </Typography>
            </CenterBox>
        </InputBox>
    )
};

export default RegisterForm;
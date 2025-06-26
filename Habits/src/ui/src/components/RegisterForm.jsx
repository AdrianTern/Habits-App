import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../hooks/authHooks';
import { useEffect, useRef, useState } from 'react';
import { styled } from '@mui/material/styles';
import { Box } from '@mui/material';
import { PrimaryButton, InputField } from '../styles/StyledComponents';
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
    const { errorMsg } = useAuthState();
    const hasError = errorMsg;

    // useForm hook to manage state of form inputs
    const { handleSubmit, control }
        = useForm();

    const usernameRef = useRef(null);

    useEffect(() => {
        if (usernameRef.current) usernameRef.current.focus();
    }, []);

    const handleOnSubmit = async (data) => {
        registerUser(data);
    }

    return (
        <InputBox component='form' onSubmit={handleSubmit(handleOnSubmit)} gap={2}>
            <Controller
                name='username'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField error={hasError} required label='Username' inputRef={usernameRef} {...field} />
                )}
            />
            <Controller
                name='password'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField type='password' required label='Password' {...field} />
                )}
            />
            <ErrorMsg hasError={hasError} errorMsg={errorMsg}/>
            <PrimaryButton
                type='submit'
                aria-label='register user'
                size='large'
            >
                Register
            </PrimaryButton>
        </InputBox>
    )
};

export default RegisterForm;
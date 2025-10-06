import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../../hooks/authHooks';
import { useEffect, useRef } from 'react';
import { InputField, PrimaryButton, InputBox, CenterBox } from '../../styles/StyledComponents';
import ErrorMsg from '../ErrorMsg';
import PasswordInput from '../PasswordInput';
import { CircularProgress } from '@mui/material';

const LoginForm = () => {
    // Get function to login user from useAuth hook
    const { loginUser } = useAuth();

    // State to indicate if the login failed and loading progress
    const { resError, loginLoading } = useAuthState();

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
                    <PasswordInput
                        error={resError}
                        label='Password'
                        ariaLabel='enter password'
                        field={field}
                    />
                )}
            />

            {/* Loading */}
            {loginLoading && (
                <CenterBox>
                    <CircularProgress color='inherit' />
                </CenterBox>
            )}

            {/* Error Message */}
            {!loginLoading && resError && <ErrorMsg errorMsg={resError} />}

            {/* Login Button */}
            <PrimaryButton
                type='submit'
                aria-label='login'
                size='large'
            >
                Continue
            </PrimaryButton>
        </InputBox>
    )
};

export default LoginForm;
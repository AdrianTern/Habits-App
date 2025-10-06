import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../../hooks/authHooks';
import { useEffect, useRef, useState } from 'react';
import { PrimaryButton, InputField, InputBox, CenterBox } from '../../styles/StyledComponents';
import ErrorMsg from '../ErrorMsg';
import PasswordInput from '../PasswordInput';
import { usePassword } from '../../hooks/passwordHooks';
import PasswordRule from '../PasswordRule';
import { CircularProgress } from '@mui/material';

const RegisterForm = () => {
    // Get function to register user from useAuth hook
    const { registerUser } = useAuth();

    // State to indicate if the registeration failed and loading progress
    const { resError, registerLoading } = useAuthState();

    // State to indicate invalid password
    const [violatedIndex, setViolatedIndex] = useState([]);

    // Validation function for password
    const { getViolatedIndex } = usePassword();

    // useForm hook to manage state of form inputs
    const { handleSubmit, control }
        = useForm();

    const usernameRef = useRef(null);

    useEffect(() => {
        if (usernameRef.current) usernameRef.current.focus();
    }, []);

    const handleOnSubmit = async (data) => {
        const index = getViolatedIndex(data?.password);
        if (index.length == 0) {
            setViolatedIndex([]);
            registerUser(data);
        } else {
            setViolatedIndex(index);
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
                    <PasswordInput
                        label='Password'
                        ariaLabel='enter password'
                        field={field}
                    />
                )}
            />

            {/* Loading */}
            {registerLoading && (
                <CenterBox>
                    <CircularProgress color='inherit' />
                </CenterBox>
            )}

            {/* Error Message */}
            {!registerLoading && resError && (
                <ErrorMsg errorMsg={resError} />
            )}

            {/* Password Rule */}
            {!registerLoading && (
                < PasswordRule violatedIndex={violatedIndex} />
            )}

            {/* Register Button */}
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
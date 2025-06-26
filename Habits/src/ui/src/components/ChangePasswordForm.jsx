import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../hooks/authHooks';
import { useEffect, useRef } from 'react';
import { styled } from '@mui/material/styles';
import { Box } from '@mui/material';
import{ PrimaryButton, InputField } from '../styles/StyledComponents';
import ErrorMsg from './ErrorMsg';

// Initialize a center box here in order for form component to work
const InputBox = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center,'
});

const ChangePasswordForm = () => {
    const { user } = useAuthState();
    const { changePassword } = useAuth();

    // useForm hook to manage state of form inputs
    const { handleSubmit, control}
        = useForm();

    // State to indicate if the password does not match
    const { errorMsg } = useAuthState();
    const hasError = errorMsg;

    const oldPasswordeRef = useRef(null);

    useEffect(() => {
        if (oldPasswordeRef.current) oldPasswordeRef.current.focus();
    }, []);

    const handleOnSubmit = async (data) => {
        changePassword(user.id, data);
    }

    return (
        <InputBox component='form' onSubmit={handleSubmit(handleOnSubmit)} gap={2}>
            <Controller
                name='oldPassword'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField type='password' error={hasError} required label='Old password' inputRef={oldPasswordeRef} {...field}/>
                )}
            />
            <Controller 
                name='newPassword'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField type='password' required label='New password' {...field}/>
                )}
            />
            <ErrorMsg hasError={hasError} errorMsg={errorMsg}/>
            <PrimaryButton
                type='submit'
                aria-label='change password'
                size='large'
            >
               Change Password
            </PrimaryButton>
        </InputBox>
    )
};

export default ChangePasswordForm;
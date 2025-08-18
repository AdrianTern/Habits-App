import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../../hooks/authHooks';
import { useEffect, useRef, useState } from 'react';
import { PrimaryButton, InputField, InputBox } from '../../styles/StyledComponents';
import ErrorMsg from '../ErrorMsg';
import { NUMBERS, STRINGS } from '../../constants';

const ChangePasswordForm = () => {
    const { user, resError } = useAuthState();
    const { changePassword } = useAuth();

    // useForm hook to manage state of form inputs
    const { handleSubmit, control }
        = useForm();

    // State to indicate if the password does not match
    const [passError, setPassError] = useState('');

    const oldPasswordeRef = useRef(null);

    useEffect(() => {
        if (oldPasswordeRef.current) oldPasswordeRef.current.focus();
    }, []);

    const handleOnSubmit = async (data) => {
        if (data.newPassword.length >= NUMBERS.MAX_PASSWORD_LENGTH) {
            setPassError('');
            changePassword(user.id, data);
        } else setPassError(STRINGS.INVALID_PASSWORD_LENGTH);
    };

    return (
        <InputBox component='form' onSubmit={handleSubmit(handleOnSubmit)} gap={2}>
            {/* Old Password Input */}
            <Controller
                name='oldPassword'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField
                        required
                        type='password'
                        error={resError}
                        label='Old password'
                        aria-label='enter old password'
                        inputRef={oldPasswordeRef}
                        {...field}
                    />
                )}
            />

            {/* New Password Input */}
            <Controller
                name='newPassword'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <InputField
                        required
                        type='password'
                        error={passError}
                        label='New password'
                        aria-label='enter new password'
                        {...field}
                    />
                )}
            />

            {/* Error Message */}
            {(resError || passError) && (
                <ErrorMsg errorMsg={resError ? resError : passError} />
            )}

            {/* Change Password Button */}
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
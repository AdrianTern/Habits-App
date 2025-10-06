import { useForm, Controller } from 'react-hook-form';
import { useAuth, useAuthState } from '../../hooks/authHooks';
import { useEffect, useRef, useState } from 'react';
import { PrimaryButton, InputBox, CenterBox } from '../../styles/StyledComponents';
import ErrorMsg from '../ErrorMsg';
import PasswordInput from '../PasswordInput';
import { usePassword } from '../../hooks/passwordHooks';
import PasswordRule from '../PasswordRule';
import { CircularProgress } from '@mui/material';

const ChangePasswordForm = () => {
    const { user, resError, changePassLoading } = useAuthState();
    const { changePassword } = useAuth();

    // useForm hook to manage state of form inputs
    const { handleSubmit, control }
        = useForm();

    // State to indicate invalid password
    const [violatedIndex, setViolatedIndex] = useState([]);

    // Validation function for password
    const { getViolatedIndex } = usePassword();

    const oldPasswordeRef = useRef(null);

    useEffect(() => {
        if (oldPasswordeRef.current) oldPasswordeRef.current.focus();
    }, []);

    const handleOnSubmit = async (data) => {
        const index = getViolatedIndex(data?.newPassword);
        if (index.length == 0) {
            setViolatedIndex([]);
            changePassword(user.id, data);
        } else {
            setViolatedIndex(index);
        }
    };

    return (
        <InputBox component='form' onSubmit={handleSubmit(handleOnSubmit)} gap={2}>
            {/* Old Password Input */}
            <Controller
                name='oldPassword'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <PasswordInput
                        error={resError}
                        label='Old password'
                        aria-label='enter old password'
                        field={field}
                        inputRef={oldPasswordeRef}
                    />
                )}
            />

            {/* New Password Input */}
            <Controller
                name='newPassword'
                control={control}
                defaultValue=''
                render={({ field }) => (
                    <PasswordInput
                        label='New password'
                        aria-label='enter new password'
                        field={field}
                    />
                )}
            />

            {/* Loading */}
            {changePassLoading && (
                <CenterBox>
                    <CircularProgress color='inherit' />
                </CenterBox>
            )}

            {/* Error Message */}
            {!changePassLoading && resError && (
                <ErrorMsg errorMsg={resError} />
            )}

            {/* Password Rule */}
            {!changePassLoading && (
                < PasswordRule violatedIndex={violatedIndex} />
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
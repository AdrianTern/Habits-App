import { NUMBERS } from "../constants"

// Custom hook to validate password
export const usePassword = () => {

    const hasNumber = (password) => {
        return /\d/.test(password);
    };

    const hasLowerCase = (password) => {
        return /[a-z]/.test(password);
    };

    const hasUpperCase = (password) => {
        return /[A-Z]/.test(password);
    };

    const hasSymbol = (password) => {
        return /[!@#$%^&*(),.?":{}|<>]/.test(password);
    };

    const hasValidLength = (password) => {
        return password?.length >= NUMBERS.MAX_PASSWORD_LENGTH;
    };

    // Returns the index of violated rules
    const getViolatedIndex = (password) => {
        const index = [];
        if (!hasUpperCase(password)) index.push(0);
        if (!hasLowerCase(password)) index.push(1)
        if (!hasNumber(password)) index.push(2);
        if (!hasSymbol(password)) index.push(3);
        if (!hasValidLength(password)) index.push(4);

        return index;
    };

    return {
        getViolatedIndex
    };
};
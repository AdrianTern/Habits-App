// Util file to navigate to different routes in app
import { ROUTES } from "../constants";
let navigator;

export const setNavigator = (nav) => {
    navigator = nav;
};

export const goToHome = () => {
    if (navigator) navigator(ROUTES.HOME);
};

export const goToLogin = () => {
    if(navigator) navigator(ROUTES.LOGIN);
};

export const goToRegister = () => {
    if(navigator) navigator(ROUTES.REGISTER);
};

export const goToChangePassword = () => {
    if(navigator) navigator(ROUTES.CHANGE_PASSWORD);
};
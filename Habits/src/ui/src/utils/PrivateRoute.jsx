import { ROUTES } from '../constants';
import { useAuthState } from '../hooks/authHooks';
import { Navigate } from "react-router-dom";

const PrivateRoute = ({ children }) => {
    const { user } = useAuthState();
    return user ? children : <Navigate to={ROUTES.LOGIN} />
};

export default PrivateRoute;
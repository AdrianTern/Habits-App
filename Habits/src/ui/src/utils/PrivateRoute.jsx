import { useAuthState } from '../hooks/authHooks';
import { Navigate } from "react-router-dom";

const PrivateRoute = ({ children }) => {
    const { user } = useAuthState();
    return user ? children : <Navigate to='/login' />
}

export default PrivateRoute;
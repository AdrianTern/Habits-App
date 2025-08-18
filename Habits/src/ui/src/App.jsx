import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import HomePage from './pages/HomePage';
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import ChangePasswordPage from "./pages/ChangePasswordPage";
import PrivateRoute from './utils/PrivateRoute';
import { ROUTES } from "./constants";
import { useEffect } from "react";
import { setNavigator } from "./utils/navigation";

const NavigatorProvider = () => {
  const navigator = useNavigate();

  useEffect(() => {
    setNavigator(navigator);
  }, [navigator]);

  return null;
};

const App = () => {
  return (
    <>
      <Router>
        <NavigatorProvider />
        <Routes>
          <Route path={ROUTES.LOGIN} element={<LoginPage />} />
          <Route path={ROUTES.REGISTER} element={<RegisterPage />} />
          <Route path={ROUTES.CHANGE_PASSWORD} element={<ChangePasswordPage />} />
          <Route
            path={ROUTES.HOME}
            element={
              <PrivateRoute>
                <HomePage />
              </PrivateRoute>
            } />
        </Routes>
      </Router>
      <ToastContainer />
    </>
  );
}

export default App;

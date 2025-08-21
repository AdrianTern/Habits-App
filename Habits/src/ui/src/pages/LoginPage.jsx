import { Typography, Link as MuiLink } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import LoginForm from '../components/auth/LoginForm';
import SecondaryAppBar from '../components/SecondaryAppBar';
import { MainBox, HeaderBox, SmallBodyBox, CenterBox } from '../styles/StyledComponents';
import { ROUTES } from '../constants';

const LoginPage = () => {
  return (
    <>
      <SecondaryAppBar />
      <MainBox>
        <HeaderBox>
          <Typography variant='h3' align='center' gutterBottom >
            Welcome
          </Typography>
        </HeaderBox>
        <SmallBodyBox>
          <LoginForm />
          <CenterBox marginTop='1rem'>
            <Typography variant='subtitle'>
              Not a user yet? {" "}
              <MuiLink component={RouterLink} to={ROUTES.REGISTER} sx={{ color: 'custom.violet' }}>
                Register
              </MuiLink>
            </Typography>
          </CenterBox>
        </SmallBodyBox>
      </MainBox>
    </>
  );
};

export default LoginPage;
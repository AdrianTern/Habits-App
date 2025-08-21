import { Typography, Link as MuiLink } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import RegisterForm from '../components/auth/RegisterForm';
import SecondaryAppBar from '../components/SecondaryAppBar';
import { MainBox, HeaderBox, SmallBodyBox, CenterBox } from '../styles/StyledComponents';
import { ROUTES } from '../constants';

const RegisterPage = () => {
  return (
    <>
      <SecondaryAppBar />
      <MainBox>
        <HeaderBox>
          <Typography variant='h3' align='center' gutterBottom >
            Sign up
          </Typography>
        </HeaderBox>
        <SmallBodyBox>
          <RegisterForm />
          <CenterBox marginTop='1rem'>
            <Typography variant='subtitle'>
              Back to {" "}
              <MuiLink component={RouterLink} to={ROUTES.LOGIN} sx={{ color: 'custom.violet' }}>
                login
              </MuiLink>
            </Typography>
          </CenterBox>
        </SmallBodyBox>
      </MainBox>
    </>
  );
};

export default RegisterPage;
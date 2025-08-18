import { Typography } from '@mui/material';
import LoginForm from '../components/auth/LoginForm';
import SecondaryAppBar from '../components/SecondaryAppBar';
import{ MainBox, HeaderBox, SmallBodyBox } from '../styles/StyledComponents';

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
        </SmallBodyBox>
      </MainBox>
    </>
  );
};

export default LoginPage;
import { Typography } from '@mui/material';
import RegisterForm from '../components/auth/RegisterForm';
import SecondaryAppBar from '../components/SecondaryAppBar';
import{ MainBox, HeaderBox, SmallBodyBox } from '../styles/StyledComponents';

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
        </SmallBodyBox>
      </MainBox>
    </>
  );
};

export default RegisterPage;
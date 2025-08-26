import { Typography } from '@mui/material';
import ChangePasswordForm from '../components/auth/ChangePasswordForm';
import{ MainBox, HeaderBox, SmallBodyBox, BodyBox } from '../styles/StyledComponents';
import MainAppBar from '../components/MainAppBar';

const ChangePasswordPage = () => {
  return (
    <>
      <MainAppBar/>
      <MainBox>
        <HeaderBox>
          <Typography variant='h3' align='center' gutterBottom >
            Change password
          </Typography>
        </HeaderBox>
        <SmallBodyBox>
          <ChangePasswordForm />
        </SmallBodyBox>
      </MainBox>
    </>
  );
};

export default ChangePasswordPage;
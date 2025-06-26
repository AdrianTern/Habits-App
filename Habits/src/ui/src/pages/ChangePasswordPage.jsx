import { Typography } from '@mui/material';
import ChangePasswordForm from '../components/ChangePasswordForm';
import SecondaryAppBar from '../components/SecondaryAppBar';
import{ MainBox, HeaderBox, SmallBodyBox } from '../styles/StyledComponents';

const ChangePasswordPage = () => {
  return (
    <>
      <SecondaryAppBar />
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
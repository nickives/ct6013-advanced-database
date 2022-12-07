import React, { useContext, useEffect, useLayoutEffect } from 'react';
import { ConfigContext } from 'features/appconfig/AppConfig';
import useRESTSubmit from 'hooks/rest-submit';
import { UserType, Student, Lecturer, StudentREST } from 'lib/types';
import { useParams, useNavigate, defer, useLoaderData } from 'react-router-dom';
import { Box, Button, Card, CardContent, FormControlLabel, Radio, RadioGroup, Typography } from '@mui/material';
import getJSON from 'lib/getJSON';
import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import * as yup from 'yup';
import ControlledSelect from 'components/ControlledSelect';
import { NavbarContext } from 'features/navbar';

interface LoginData {
  userId: string;
  userType: UserType;
}

interface LoginResult {
  userId: string;
  name: string;
  courseId?: string;
  destination: string;
}

const loginPages = [
  { name: 'Admin', path: '/admin' },
  { name: 'Register Student', path: '/register-student' },
];

export const loginLoader = async () => {
  const students = getJSON<StudentREST[]>('/api/student')
    .then((data) => data.map((student) => ({
      ...student, name: `${student.firstName} ${student.lastName}`,
    })));
  const lecturers = getJSON<Lecturer[]>('/api/lecturer');
  return defer({ students, lecturers });
};

interface LoginLoaderData {
  students: Student[];
  lecturers: Lecturer[];
}

const schema = yup.object({
  userId: yup.string().required('User ID is required'),
});

const Login = (): JSX.Element => {
  const { userId } = useParams();
  const [loading, error, data, submitFn] = useRESTSubmit<LoginResult, LoginData>();
  const { loginState, setLoginState } = useContext(ConfigContext);
  const { updatePages } = useContext(NavbarContext);
  const navigate = useNavigate();
  const { students, lecturers } = useLoaderData() as LoginLoaderData;
  const [loginType, setLoginType] = React.useState(UserType.STUDENT);
  const {
    control, handleSubmit, getValues, formState: { errors },
  } = useForm<LoginData>({ resolver: yupResolver(schema) });

  useLayoutEffect(() => {
    updatePages(loginPages);
    return () => updatePages([]);
  }, [updatePages]);

  useEffect(() => {
    if (userId) {
      submitFn({ userId: userId, userType: UserType.STUDENT }, '/api/login');
    } else if (loginState) {
      navigate(loginState.destination);
    }
  });

  useEffect(() => {
    if (data) {
      setLoginState({
        userId: data.userId,
        name: data.name,
        courseId: data.courseId,
        destination: data.destination,
      });
      navigate(data.destination);
    }
  });

  return (
    <Box
      component="form"
      onSubmit={ handleSubmit(() => submitFn({
        ...getValues(),
        userType: loginType,
      }, '/api/login')) }
      sx={ {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        height: '100%',
        justifyContent: 'space-between',
        paddingTop: 2,
      } }
    >
      <Card raised className="mb-5">
        <CardContent>
          <Typography variant="h4">Login</Typography>
        </CardContent>
      </Card>
      <Card raised>
        <CardContent className="grid grid-cols-3 gap-5">
          <Box>
            <RadioGroup
              defaultValue="STUDENT"
              onChange={ (e) => setLoginType(e.target.value as UserType) }
            >
              <FormControlLabel value="STUDENT" control={ <Radio /> } label="Student" />
              <FormControlLabel value="LECTURER" control={ <Radio /> } label="Lecturer" />
            </RadioGroup>
          </Box>
          <Box>
            <ControlledSelect
              data={ loginType === UserType.STUDENT ? students : lecturers }
              control={ control }
              errorMessage={ errors?.userId?.message }
              loadingMessage="Loading users..."
              label="User"
              name="userId"
              rules={ { required: true } }
              defaultValue=""
              id="userId"
              dataTestId="user-select"
              menuItemKey="id"
              menuItemValue="id"
              menuItemText="name"
            />
          </Box>
          <Box>
            <Button type="submit" disabled={ loading }>Login</Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Login;

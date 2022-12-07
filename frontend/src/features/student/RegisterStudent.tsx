import React, { useContext, useLayoutEffect } from 'react';

// LIBRARIES
import { Alert, Box, Button, Card, CardContent, TextField, Typography } from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';

// COMPONENTS
import useRESTSubmit from 'hooks/rest-submit';
import ControlledSelect from 'components/ControlledSelect';
import getJSON from 'lib/getJSON';
import { defer, Link, useLoaderData } from 'react-router-dom';
import { Course } from 'lib/types';
import { NavbarContext } from 'features/navbar';

interface FormData {
  firstName: string;
  lastName: string;
  courseId: string;
}

interface ResultData {
  id: string;
  firstName: string;
  lastName: string;
  number: string;
}

interface RegisterStudentData {
  courses: Course[];
}

const schema = yup.object({
  firstName: yup.string().required('First name is required'),
  lastName: yup.string().required('Last name is required'),
  courseId: yup.string().required('Pick a course'),
}).required();

export const registerStudentLoader = async () => {
  const courses = getJSON<Course[]>('/api/course');
  return defer({ courses });
};

const loginPages = [
  { name: 'Logout', path: '/logout' },
];

const RegisterStudent = () => {
  const {
    control, handleSubmit, getValues, formState: { errors },
  } = useForm<FormData>({ resolver: yupResolver(schema) });
  const [loading, error, data, submitFn] = useRESTSubmit<ResultData, FormData>();
  const { courses } = useLoaderData() as RegisterStudentData;
  const { updatePages } = useContext(NavbarContext);

  useLayoutEffect(() => {
    updatePages(loginPages);
    return () => updatePages([]);
  }, [updatePages]);

  return (
    <Box
      component="form"
      onSubmit={ handleSubmit(() => submitFn(getValues(), '/api/student')) }
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
          <Typography variant="h4">Register Student</Typography>
        </CardContent>
      </Card>
      <Card raised>
        <CardContent>
          <Box className="grid grid-cols-3 gap-5">
            <Controller
              name="firstName"
              control={ control }
              rules={ { required: true } }
              defaultValue=""
              render={ ({ field }) => (
                <TextField
                  // eslint-disable-next-line react/jsx-props-no-spreading
                  { ...field }
                  variant="filled"
                  label="First Name"
                  error={ errors?.firstName?.message !== undefined }
                  helperText={ errors?.firstName?.message }
                />
              ) }
            />
            <Controller
              name="lastName"
              control={ control }
              rules={ { required: true } }
              defaultValue=""
              render={ ({ field }) => (
                <TextField
                  // eslint-disable-next-line react/jsx-props-no-spreading
                  { ...field }
                  variant="filled"
                  label="Last Name"
                  error={ errors?.lastName?.message !== undefined }
                  helperText={ errors?.lastName?.message }
                />
              ) }
            />
            <ControlledSelect
              data={ courses }
              control={ control }
              errorMessage={ errors?.courseId?.message }
              loadingMessage="Loading courses..."
              label="Course"
              name="courseId"
              rules={ { required: true } }
              defaultValue=""
              id="courseId"
              dataTestId="course-select"
              menuItemKey="id"
              menuItemValue="id"
              menuItemText="name"
            />
            <Box />
            <Box />
            <Button type="submit" disabled={ loading }>Submit</Button>
          </Box>
          {
            error
              ? <Alert className="mt-5" severity="error">{error.message}</Alert>
              : null
          }
          {
            data
              ? (
                <Alert className="mt-5" severity="success">
                  Registered student {data.firstName} {data.lastName}. <Button component={ Link } to={ `/login/${data.id}` }>Login?</Button>
                </Alert>
              )
              : null
          }
        </CardContent>
      </Card>
    </Box>
  );
};

export default RegisterStudent;

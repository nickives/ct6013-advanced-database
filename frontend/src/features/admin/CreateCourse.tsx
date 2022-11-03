import React from 'react';
import { Alert, Box, Button, Card, CardContent, Container, TextField, Typography } from '@mui/material';
import { yupResolver } from '@hookform/resolvers/yup';
import useRESTSubmit from 'hooks/rest-submit';
import { Controller, useForm } from 'react-hook-form';
import * as yup from 'yup';

interface FormData {
  name: string;
}

interface ResultData {
  id: string;
  name: string;
}

const schema = yup.object({
  name: yup.string().required(),
}).required();

const CreateCourse = () => {
  const {
    control, handleSubmit, getValues, formState: { errors },
  } = useForm<FormData>({ resolver: yupResolver(schema) });
  const [loading, error, data, submitFn] = useRESTSubmit<ResultData, FormData>('/api/create-course');

  return (
    <Box
      component="form"
      onSubmit={ handleSubmit(() => submitFn(getValues())) }
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
          <Typography variant="h4">Create Course</Typography>
        </CardContent>
      </Card>
      <Card raised>
        <CardContent>
          <Box display="flex">
            <Controller
              name="name"
              control={ control }
              rules={ { required: true } }
              defaultValue=""
              render={ ({ field }) => (
                <TextField
                  // eslint-disable-next-line react/jsx-props-no-spreading
                  { ...field }
                  variant="filled"
                  label="Name"
                  error={ errors?.name?.message !== undefined }
                  helperText={ errors?.name?.message }
                />
              ) }
            />
            <Button type="submit" disabled={ loading }>Submit</Button>
          </Box>
          {
            error
              ? <Alert severity="error">{error.message}</Alert>
              : null
          }
          {
            data
              ? <Alert severity="success">Created course {data.name} with id {data.id}</Alert>
              : null
          }
        </CardContent>
      </Card>
    </Box>
  );
};

export default CreateCourse;

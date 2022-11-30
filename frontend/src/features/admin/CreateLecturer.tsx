import React from 'react';

// LIBRARIES
import { Alert, Box, Button, Card, CardContent, Container, FormControl, InputAdornment, TextField, Typography } from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';

// COMPONENTS
import useRESTSubmit from 'hooks/rest-submit';

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

const CreateLecturer = () => {
  const {
    control, handleSubmit, getValues, formState: { errors },
  } = useForm<FormData>({ resolver: yupResolver(schema) });
  const [loading, error, data, submitFn] = useRESTSubmit<ResultData, FormData>();

  return (
    <Box
      component="form"
      onSubmit={ handleSubmit(() => submitFn(getValues(), '/api/lecturer')) }
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
          <Typography variant="h4">Create Lecturer</Typography>
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
              ? <Alert severity="success">Created lecturer {data.name} with id {data.id}</Alert>
              : null
          }
        </CardContent>
      </Card>
    </Box>
  );
};

export default CreateLecturer;

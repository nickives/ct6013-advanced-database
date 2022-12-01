import React, { Suspense } from 'react';

// LIBRARIES
import { Alert, Box, Button, Card, CardContent, FormControl, InputLabel, MenuItem, NativeSelect, Select, TextField, Typography } from '@mui/material';
import { yupResolver } from '@hookform/resolvers/yup';
import { Control, Controller, FieldValues, useForm } from 'react-hook-form';
import { Await, defer, useLoaderData } from 'react-router-dom';
import * as yup from 'yup';

// APP
import useRESTSubmit from 'hooks/rest-submit';
import { Lecturer, Course } from 'lib/types';
import getJSON from 'lib/getJSON';

import ControlledSelect from 'components/ControlledSelect';

interface FormData {
  name: string;
  code: string;
  courseId: string;
  lecturerId: string;
  catPoints: number;
  semester: string;
}

type SubmitData = Omit<FormData, 'courseId'>;

interface ResultData {
  id: string;
  name: string;
}

const schema = yup.object({
  name: yup.string().required('Module Name is required'),
  code: yup.string().required('Module code is required'),
  courseId: yup.string().required('Pick a course'),
  lecturerId: yup.string().required('Pick a lecturer'),
  catPoints: yup.number().integer().min(1, 'CAT points are required').required('CAT points are required'),
  semester: yup.string().required('Semester is required'),
}).required();

export interface CreateModuleData {
  lecturers: Lecturer[];
  courses: Course[];
}

interface PagenationProps {
  page: number;
  limit: number;
}

export const createModuleLoader = async () => {
  const lecturers = getJSON<Lecturer[]>('/api/lecturer');
  const courses = getJSON<Course[]>('/api/course');
  return defer({ lecturers, courses });
};

const CreateModule = () => {
  const {
    control, handleSubmit, getValues, formState: { errors },
  } = useForm<FormData>({ resolver: yupResolver(schema) });
  const [loading, error, data, submitFn] = useRESTSubmit<ResultData, SubmitData>();
  const { lecturers, courses } = useLoaderData() as CreateModuleData;

  return (
    <Box
      component="form"
      onSubmit={ handleSubmit(() => {
        const values = getValues();
        const { courseId: _, ...submitValues } = values;
        return submitFn(submitValues, `/api/course/${values.courseId}/modules`);
      }) }
      sx={ {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        height: '100%',
        justifyContent: 'space-between',
        paddingTop: 2,
      } }
      className="w-screen"
    >
      <Card raised className="mb-5">
        <CardContent>
          <Typography variant="h4">Create Module</Typography>
        </CardContent>
      </Card>
      <Card raised className="w-3/4">
        <CardContent>
          <Box className="grid gap-5 grid-cols-3">
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
                  label="Module Name"
                  error={ errors?.name?.message !== undefined }
                  helperText={ errors?.name?.message }
                />
              ) }
            />
            <Box>
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
            </Box>
            <Box>
              <ControlledSelect
                data={ lecturers }
                control={ control }
                errorMessage={ errors?.lecturerId?.message }
                loadingMessage="LecturerLoading"
                label="Lecturer"
                name="lecturerId"
                rules={ { required: true } }
                defaultValue=""
                id="lecturer"
                dataTestId="lecturer-select"
                menuItemKey="id"
                menuItemValue="id"
                menuItemText="name"
              />
            </Box>
            <Controller
              name="code"
              control={ control }
              rules={ { required: true } }
              defaultValue=""
              render={ ({ field }) => (
                <TextField
                  // eslint-disable-next-line react/jsx-props-no-spreading
                  { ...field }
                  variant="filled"
                  label="Module Code"
                  error={ errors?.code?.message !== undefined }
                  helperText={ errors?.code?.message }
                />
              ) }
            />
            <Controller
              name="catPoints"
              control={ control }
              rules={ { required: true } }
              defaultValue={ 0 }
              render={ ({ field }) => (
                <TextField
                  // eslint-disable-next-line react/jsx-props-no-spreading
                  { ...field }
                  // This is a hack to make the input type number
                  onChange={ (event) => field.onChange(+event.target.value) }
                  variant="filled"
                  label="CAT Points"
                  inputProps={ { inputMode: 'numeric', pattern: '[0-9]*' } }
                  error={ errors?.catPoints?.message !== undefined }
                  helperText={ errors?.catPoints?.message }
                />
              ) }
            />
            <Controller
              name="semester"
              control={ control }
              rules={ { required: true } }
              defaultValue=""
              render={ ({ field }) => (
                <TextField
                  // eslint-disable-next-line react/jsx-props-no-spreading
                  { ...field }
                  variant="filled"
                  label="Semester"
                  error={ errors?.semester?.message !== undefined }
                  helperText={ errors?.semester?.message }
                />
              ) }
            />
            <Box sx={ { gridColumn: '1/3' } }>
              {
              error
                ? <Alert severity="error">{error.message}</Alert>
                : null
              }
              {
                data
                  ? <Alert severity="success">Created module {data.name} with id {data.id}</Alert>
                  : null
              }
            </Box>
            <Button type="submit" disabled={ loading }>Submit</Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default CreateModule;

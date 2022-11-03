import React, { Suspense } from 'react';

// LIBRARIES
import { Alert, Box, Button, Card, CardContent, FormControl, InputLabel, MenuItem, NativeSelect, Select, TextField, Typography } from '@mui/material';
import { yupResolver } from '@hookform/resolvers/yup';
import { Controller, useForm } from 'react-hook-form';
import { Await, defer, useLoaderData } from 'react-router-dom';
import * as yup from 'yup';

// APP
import useRESTSubmit from 'hooks/rest-submit';
import { Lecturer, Course } from 'types';
import postJSON from 'lib/postJSON';

interface FormData {
  name: string;
  code: string;
  courseId: string;
  lecturerId: string;
  catPoints: number;
  semester: string;
}

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
  const lecturers = postJSON<Lecturer[]>('/api/lecturers', { page: 0, limit: 100 });
  const courses = postJSON<Course[]>('/api/courses', { page: 0, limit: 100 });
  return defer({ lecturers, courses });
};

const CreateModule = () => {
  const {
    control, handleSubmit, getValues, formState: { errors },
  } = useForm<FormData>({ resolver: yupResolver(schema) });
  const [loading, error, data, submitFn] = useRESTSubmit<ResultData, FormData>('/api/create-module');
  const { lecturers, courses } = useLoaderData() as CreateModuleData;

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
              <Suspense fallback={ <>😸CourseLoading😸</> }>
                <Await resolve={ courses } errorElement={ <>😿</> }>
                  { (coursesArg: Course[]) => (
                    <FormControl fullWidth>
                      <Controller
                        name="courseId"
                        control={ control }
                        rules={ { required: true } }
                        defaultValue=""
                        render={ ({ field }) => (
                          <>
                            <InputLabel id="course-select-label">Course</InputLabel>
                            <Select
                              // eslint-disable-next-line react/jsx-props-no-spreading
                              { ...field }
                              labelId="course-select-label"
                              id="courseId"
                              label="Course"
                              error={ errors?.courseId?.message !== undefined }
                              data-testid="course-select"
                            >
                              {coursesArg.map((course: Course) => (
                                <MenuItem key={ course.id } value={ course.id }>
                                  {course.name}
                                </MenuItem>
                              )) }
                            </Select>
                          </>
                        ) }
                      />
                    </FormControl>
                  ) }
                </Await>
              </Suspense>
            </Box>
            <Box>
              <Suspense fallback={ <>😸LecturerLoading😸</> }>
                <Await resolve={ lecturers } errorElement={ <>😿</> }>
                  { (lecturersArg: Lecturer[]) => (
                    <FormControl fullWidth>
                      <InputLabel>Lecturer</InputLabel>
                      <Controller
                        name="lecturerId"
                        control={ control }
                        rules={ { required: true } }
                        defaultValue=""
                        render={ ({ field }) => (
                          <Select
                            // eslint-disable-next-line react/jsx-props-no-spreading
                            { ...field }
                            labelId="lecturer"
                            id="lecturer"
                            label="Lecturer"
                            error={ errors?.lecturerId?.message !== undefined }
                            data-testid="lecturer-select"
                          >
                            {lecturersArg.map((lecturer: Lecturer) => (
                              <MenuItem key={ lecturer.id } value={ lecturer.id }>
                                {lecturer.name}
                              </MenuItem>
                            )) }
                          </Select>
                        ) }
                      />
                    </FormControl>
                  ) }
                </Await>
              </Suspense>
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

import React, { Suspense, useContext, useEffect, useLayoutEffect, useState } from 'react';
import { Box } from '@mui/system';
import { ModuleMarks } from 'lib/types';
import { Alert, Button, Card, CardContent, Paper, TextField, Typography } from '@mui/material';
import { ConfigContext } from 'features/appconfig/AppConfig';
import getJSON from 'lib/getJSON';
import { Await, useParams } from 'react-router-dom';
import { Control, Controller, FieldErrorsImpl, useFieldArray, useForm, UseFormRegister } from 'react-hook-form';
import useRESTSubmit from 'hooks/rest-submit';

interface StudentMarkPaperProps {
  firstName: string;
  lastName: string;
  mark: number;
  register: UseFormRegister<MarkModuleForm>;
  control: Control<MarkModuleForm, any>;
  index: number;
  errors: Partial<FieldErrorsImpl<{
    marks: {
        studentId: string;
        mark: number;
    }[];
  }>>
}

const StudentMarkPaper = ({
  firstName, lastName, mark, register, control, index, errors,
}: StudentMarkPaperProps) => (
  <Paper className="grid grid-cols-4 gap-5 mb-5 p-2" elevation={ 12 }>
    <Box className="col-span-3">
      Student: {firstName} {lastName}
    </Box>
    <Box>
      <input type="hidden" { ...register(`marks.${index}.studentId` as const) } />
      <Controller
        name={ `marks.${index}.mark` as const }
        control={ control }
        defaultValue={ mark ?? '' }
        render={ ({ field }) => (
          <TextField
            // eslint-disable-next-line react/jsx-props-no-spreading
            { ...field }
            label="Mark"
            inputProps={ { inputMode: 'numeric', pattern: '[0-9]*' } }
            error={ !!errors.marks?.[index]?.mark }
          />
        ) }
      />
    </Box>
  </Paper>
);

interface MarkModuleForm {
  marks: {
    studentId: string;
    mark: number;
  }[];
}

interface MarkModuleResult {
  result: string;
}

const MarkModule = () => {
  const { moduleId } = useParams();
  const [studentMarks, setStudentMarks] = useState<ModuleMarks>();
  const { loginState } = useContext(ConfigContext);
  const {
    control, handleSubmit, getValues, register, formState: { errors },
  } = useForm<MarkModuleForm>();

  const [loading, submitError, data, submitFn] = useRESTSubmit<MarkModuleResult, MarkModuleForm>();

  const { replace } = useFieldArray({ control: control, name: 'marks' });

  useLayoutEffect(() => {
    if (studentMarks) {
      replace(studentMarks.studentMarks.map((mark) => ({
        studentId: mark.studentId,
        mark: mark.mark,
      })));
    }
  }, [replace, studentMarks]);

  useEffect(() => {
    if (loginState) {
      const fetchModuleMarks = async () => {
        const moduleData = await getJSON<ModuleMarks>(`/api/lecturer/${loginState.userId}/modules/${moduleId}`);
        setStudentMarks(moduleData);
      };
      fetchModuleMarks();
    }
  }, [loginState, moduleId]);

  return (
    <Box
      component="form"
      onSubmit={ handleSubmit(() => submitFn(getValues(), `/api/lecturer/${loginState?.userId}/modules/${moduleId}`)) }
      sx={ {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        height: '100%',
        justifyContent: 'space-between',
        paddingTop: 2,
      } }
    >
      <fieldset disabled={ loading }>
        <Suspense fallback={ <>😸Loading😸</> }>
          <Await resolve={ studentMarks } errorElement={ <>😿Error😿</> }>
            <Card raised className="mb-5">
              <CardContent>
                <Typography variant="h3">Mark - {studentMarks?.module.name}</Typography>
              </CardContent>
            </Card>
            <Card raised>
              <CardContent>
                <Box className="grid grid-cols-8 gap-5">
                  <Box className="col-span-6">
                    {studentMarks?.studentMarks.map(
                      ({ firstName, lastName, mark, studentId }, index) => (
                        <StudentMarkPaper
                          control={ control }
                          index={ index }
                          register={ register }
                          key={ studentId }
                          mark={ mark }
                          firstName={ firstName }
                          lastName={ lastName }
                          errors={ errors }
                        />
                      ),
                    ) }
                  </Box>
                  <Box className="col-span-2 text-center">
                    <Paper elevation={ 12 }>
                      <Button type="submit" disabled={ loading }>Submit</Button>
                    </Paper>
                    {
                      data?.result === 'OK'
                        ? (
                          <Paper elevation={ 12 } className="gap-5 mt-2">
                            <Alert severity="success">Marks submitted!</Alert>
                          </Paper>
                        )
                        : undefined
                    }
                    {
                      submitError?.message
                        ? (
                          <Paper elevation={ 12 } className="gap-5 mt-2">
                            <Alert severity="error">{submitError.message}</Alert>
                          </Paper>
                        )
                        : undefined
                    }
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Await>
        </Suspense>
      </fieldset>
    </Box>
  );
};

export default MarkModule;

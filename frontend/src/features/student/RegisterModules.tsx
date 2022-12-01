import React, { useContext, useEffect, useLayoutEffect, useState } from 'react';
import { Box, Button, Card, CardContent, Paper, Typography } from '@mui/material';
import { ConfigContext } from 'features/appconfig/AppConfig';
import useRESTSubmit from 'hooks/rest-submit';
import getJSON from 'lib/getJSON';
import { Module } from 'lib/types';
import { useFieldArray, useForm, UseFormRegister } from 'react-hook-form';

type FormModule = Module & { checked?: boolean };

interface RegisterModulesFormData {
  modules: FormModule[] ;
}

interface ModuleRESTSubmit {
  moduleIds: string[];
}

type RegisterModulesResult = ModuleRESTSubmit;

interface ModuleCardProps {
  module: Module;
  index: number;
  register: UseFormRegister<RegisterModulesFormData>;
}

const ModulePaper = ({ module, index, register }: ModuleCardProps) => (
  <Paper key={ module.id } className="grid grid-cols-7 gap-5 mb-5 p-2" elevation={ 12 }>
    <Box className="col-span-7">
      {`${module.name}: ${module.code}`}
    </Box>
    <Box>
      <input
        type="checkbox"
        { ...register(`modules.${index}.checked` as const) }
      />
    </Box>
    <Box className="col-span-3">
      CAT Points: {module.catPoints}
    </Box>
    <Box className="col-span-3">
      Semester: {module.semester}
    </Box>
  </Paper>
);

const RegisterModules = (): JSX.Element => {
  const [modules, updateModules] = useState<Module[]>([]);
  const { loginState } = useContext(ConfigContext);
  const [catPointsTotal, setCatPointsTotal] = useState(0);
  const [successfulSubmit, setSuccessfulSubmit] = useState(false);

  const {
    control, handleSubmit, getValues, register, formState: { errors },
  } = useForm<RegisterModulesFormData>();

  const { fields, replace } = useFieldArray({ control: control, name: 'modules' });

  const [
    loading, error, data, submitFn,
  ] = useRESTSubmit<RegisterModulesResult, ModuleRESTSubmit>();

  useLayoutEffect(() => {
    if (modules) {
      replace(modules);
    }
  }, [modules, replace]);

  useEffect(() => {
    if (loginState) {
      const fetchModules = async () => {
        // eslint-disable-next-line quotes
        const moduleData = await getJSON<Module[]>(`/api/course/${loginState.courseId}/modules`);
        updateModules(moduleData);
      };
      fetchModules();
    }
  }, [loginState]);

  useLayoutEffect(() => {
    if (data) {
      const success = data.moduleIds
        ?.every((id) => getValues().modules.find((module) => module.id === id));
      setSuccessfulSubmit(success);
    }
  }, [data, getValues]);

  return (
    <Box
      component="form"
      onSubmit={ handleSubmit(() => {
        setSuccessfulSubmit(false);
        const checked = getValues().modules
          .flatMap((module) => (module.checked ? module.id : []));
        submitFn({ moduleIds: checked }, `/api/student/${loginState?.userId}/modules`);
      }) }
      onChange={ () => {
        const newCatPointsTotal = getValues().modules
          .filter((module) => module.checked)
          .reduce((acc, module) => acc + module.catPoints, 0);
        setCatPointsTotal(newCatPointsTotal);
      } }
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
          <Typography variant="h3">Choose Modules</Typography>
        </CardContent>
      </Card>
      <Card raised>
        <CardContent>
          <Box className="grid grid-cols-7 gap-5">
            <Box className="col-span-4">
              { fields.map((field, index) => (
                <ModulePaper
                  key={ field.id }
                  module={ field }
                  index={ index }
                  register={ register }
                />
              )) }
            </Box>
            <Box className="col-span-3">
              <Paper elevation={ 12 } className="mb-5 p-2">
                <Box className="mb-10">
                  Cat Points Selected: {catPointsTotal}
                </Box>
                <Box>
                  <Button type="submit">Submit</Button>
                </Box>
              </Paper>
              <Box>
                {errors.modules && <Paper elevation={ 24 } className="mb-5 p-2">{errors.modules.message}</Paper>}
                {error && <Paper elevation={ 24 } className="mb-5 p-2">{error.message}</Paper>}
                {successfulSubmit && <Paper elevation={ 24 } className="mb-5 p-2">Successfully submitted</Paper>}
              </Box>
            </Box>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default RegisterModules;

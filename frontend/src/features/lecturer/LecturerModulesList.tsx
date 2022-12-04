import React, { useContext, useEffect, useState } from 'react';
import { Box, Button, Card, CardContent, Paper, Typography } from '@mui/material';
import { Module } from 'lib/types';
import { ConfigContext } from 'features/appconfig/AppConfig';
import getJSON from 'lib/getJSON';
import { Link } from 'react-router-dom';

const LecturerModulesList = () => {
  const { loginState } = useContext(ConfigContext);
  const [modules, setModules] = useState<Module[]>([]);

  useEffect(() => {
    if (loginState) {
      const fetchModules = async () => {
        const moduleData = await getJSON<Module[]>(`/api/lecturer/${loginState.userId}/modules`);
        setModules(moduleData);
      };
      fetchModules();
    }
  }, [loginState]);

  return (
    <Box
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
          <Typography variant="h3">Your Modules</Typography>
        </CardContent>
      </Card>
      <Card raised>
        <CardContent>
          {modules.map((module) => (
            <Paper key={ module.id } elevation={ 12 } className="grid grid-cols-3 m-3 p2">
              <Typography>{module.name}: {module.code}</Typography>
              <Typography>Semester: {module.semester}</Typography>
              <Button component={ Link } to={ `/lecturer/mark/${module.id}` }>Mark</Button>
            </Paper>
          )) }
        </CardContent>
      </Card>
    </Box>
  );
};

export default LecturerModulesList;

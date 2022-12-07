import React, { useContext, useEffect, useState } from 'react';
import { StudentResultREST } from 'lib/types';
import { Box, Card, CardContent, Paper, Typography } from '@mui/material';
import { ConfigContext } from 'features/appconfig/AppConfig';
import getJSON from 'lib/getJSON';

const ViewResults = () => {
  const { loginState } = useContext(ConfigContext);
  const [results, setResults] = useState<StudentResultREST>();

  useEffect(() => {
    if (loginState) {
      const fetchModules = async () => {
        // eslint-disable-next-line quotes
        const courseResult = await getJSON<StudentResultREST>(`/api/student/${loginState.userId}/results`);
        setResults(courseResult);
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
          <Typography variant="h3">Results</Typography>
        </CardContent>
      </Card>

      <Card raised className="mb-5">
        <CardContent
          sx={ {
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            height: '100%',
            justifyContent: 'space-between',
            paddingTop: 2,
          } }
        >
          <Paper elevation={ 24 } className="b-5 p-2 mb-5">
            <Typography variant="h4">Course results</Typography>
          </Paper>
          <Paper elevation={ 12 } className="b-5 p-2 mb-5">
            <Typography variant="h5">Average Grade: {results?.averageMark}</Typography>
          </Paper>
          <Paper elevation={ 12 } className="b-5 p-2">
            <Typography variant="h5">Final Grade: {results?.grade || '--'}</Typography>
          </Paper>
        </CardContent>
      </Card>

      <Card raised>
        <CardContent
          sx={ {
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'centre',
            height: '100%',
            justifyContent: 'space-between',
            paddingTop: 2,
          } }
        >
          <Paper elevation={ 24 } className="b-5 p-2">
            <Typography variant="h4">Module results</Typography>
          </Paper>
          {results?.moduleResults.map((result) => (
            <Paper key={ result.module.id } elevation={ 12 } className="m-3 p-2 grid grid-cols-4">
              <Typography variant="h5" className="col-span-3">{result.module.name}</Typography>
              <Typography variant="h5" className="col-span-1">Grade: {result.mark || '--'}</Typography>
            </Paper>
          )) }
        </CardContent>
      </Card>
    </Box>
  );
};

export default ViewResults;

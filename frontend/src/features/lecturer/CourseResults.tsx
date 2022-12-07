import { ExpandLess, ExpandMore } from '@mui/icons-material';
import { Box, Card, CardContent, Collapse, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import getJSON from 'lib/getJSON';
import { CourseResultsREST } from 'lib/types';
import React, { Suspense, useEffect, useReducer, useState } from 'react';
import { Await } from 'react-router-dom';

interface CourseCardProps {
  course: CourseResultsREST;
  moduleViewState: ModuleViewState;
  dispatchModuleViewState: React.Dispatch<ModuleViewState>;
}

const CourseCard = ({ course, moduleViewState, dispatchModuleViewState }: CourseCardProps) => (
  <Card raised className="mb-5">
    <CardContent className="grid grid-cols-6 gap-4">
      <Paper elevation={ 12 } className="col-span-3">
        <Typography variant="h4" className="p-2">{course.courseName}</Typography>
      </Paper>
      <Box />
      <Paper elevation={ 12 } className="col-span-2">
        <Typography variant="h6" className="p-2">Average Mark: {course.averageMark.toFixed(0)}</Typography>
      </Paper>
      <Paper elevation={ 12 } className="col-span-6">
        <Box>
          <Typography variant="h6" className="p-2">Grade Outcomes</Typography>
        </Box>
        <Box>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell><Typography variant="h6" className="p-2">First</Typography></TableCell>
                  <TableCell><Typography variant="h6" className="p-2">Two:One</Typography></TableCell>
                  <TableCell><Typography variant="h6" className="p-2">Two:Two</Typography></TableCell>
                  <TableCell><Typography variant="h6" className="p-2">Third</Typography></TableCell>
                  <TableCell><Typography variant="h6" className="p-2">Fail</Typography></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                <TableRow>
                  <TableCell>
                    <Typography variant="h6" className="p-2">{course.firstGrades}</Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="h6" className="p-2">{course.twoOneGrades}</Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="h6" className="p-2">{course.twoTwoGrades}</Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="h6" className="p-2">{course.thirdGrades}</Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="h6" className="p-2">{course.failGrades}</Typography>
                  </TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </TableContainer>
        </Box>
      </Paper>
      <Paper elevation={ 12 } className="col-span-6">
        <Box className="grid grid-cols-7">
          <Box className="col-span-6">
            <Typography variant="h6" className="p-2">Module Results</Typography>
          </Box>
          <Box onClick={ () => {
            const newState = moduleViewState[course.courseId] !== undefined
              ? !moduleViewState[course.courseId]
              : true;
            dispatchModuleViewState({ [course.courseId]: newState });
          } }
          >
            { moduleViewState[course.courseId] ? <ExpandLess fontSize="large" /> : <ExpandMore fontSize="large" />}
          </Box>
        </Box>
        <Collapse in={ moduleViewState[course.courseId] }>
          <Box>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Module Name</TableCell>
                    <TableCell>Average Mark</TableCell>
                    <TableCell>Number of Students</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {course.moduleStats.map((module) => (
                    <TableRow key={ module.moduleId }>
                      <TableCell>{module.moduleName}</TableCell>
                      <TableCell>{module.averageMark ? module.averageMark.toFixed(0) : '--'}</TableCell>
                      <TableCell>{module.numberOfStudents}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        </Collapse>
      </Paper>
    </CardContent>
  </Card>
);

interface ModuleViewState {
  [key: string]: boolean;
}

const moduleViewStateReducer = (state: ModuleViewState, action: ModuleViewState)
: ModuleViewState => {
  const newState = { ...state };
  Object.keys(action).forEach((key) => {
    newState[key] = action[key];
  });
  return newState;
};

const CourseResults = () => {
  const [results, setResults] = useState<CourseResultsREST[]>();
  const [
    moduleViewState, dispatchModuleViewState,
  ] = useReducer(moduleViewStateReducer, {});

  useEffect(() => {
    const fetchResults = async () => {
      const res = await getJSON<CourseResultsREST[]>('/api/course-stats');
      setResults(res);
      dispatchModuleViewState({});
    };
    fetchResults();
  }, []);

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
          <Typography variant="h4">Course Results</Typography>
        </CardContent>
      </Card>
      <Suspense fallback={ <>😸Loading!😸</> }>
        <Await resolve={ results } errorElement={ <>😿Error😿</> }>
          { results?.map((course) => (
            <CourseCard
              key={ course.courseId }
              course={ course }
              moduleViewState={ moduleViewState }
              dispatchModuleViewState={ dispatchModuleViewState }
            />
          )) }
        </Await>
      </Suspense>
    </Box>
  );
};

export default CourseResults;

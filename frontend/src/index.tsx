import React, { useContext } from 'react';
import ReactDOM from 'react-dom/client';

// LIBRARIES
import { createBrowserRouter, RouterProvider, createRoutesFromElements, Route, Navigate } from 'react-router-dom';

// COMPONENTS
import { CreateCourse, CreateModule, CreateLecturer, createModuleLoader } from 'features/admin';
import { NavbarProvider } from 'features/navbar';
import { AdminRoot, AdminIndex, ErrorPage, Root, NoMatch, StudentRoot, StudentIndex, RequireAuth } from 'routes';
import RegisterStudent, { registerStudentLoader } from 'features/student/RegisterStudent';
import Login, { loginLoader } from 'features/login/Login';
import ConfigProvider, { ConfigContext } from 'features/appconfig/AppConfig';
import { RegisterModules } from 'features/student';
import ViewResults from 'features/student/ViewResults';
import { LecturerIndex, LecturerRoot } from 'routes/Lecturer';
import LecturerModulesList from 'features/lecturer/LecturerModulesList';
import MarkModule from 'features/lecturer/MarkModule';
import CourseResults from 'features/lecturer/CourseResults';

// LOCAL
import './index.css';
import reportWebVitals from './reportWebVitals';

// if we're directly connecting to the hot-reloading dev server, use mock data
// if (window.location.port === '3000' || process.env.NODE_ENV === 'development') {
if (window.location.port === '3000' || (
  process.env.NODE_ENV === 'development' && window.location.hostname !== 'localhost'
)) {
  // eslint-disable-next-line @typescript-eslint/no-var-requires, global-require
  const { worker } = require('./mocks/browser');
  worker.start({
    onUnhandledRequest: 'warn',
  });
}

const Logout = () => {
  const { setLoginState } = useContext(ConfigContext);
  setLoginState(undefined);
  return <Navigate to="/" />;
};

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={ <Root /> } errorElement={ <ErrorPage /> }>
      <Route index element={ <Login /> } loader={ loginLoader } />
      <Route path="/logout" element={ <Logout /> } />
      <Route path="/admin" element={ <AdminRoot /> }>
        <Route index element={ <AdminIndex /> } />
        <Route path="create-course" element={ <CreateCourse /> } />
        <Route path="create-module" element={ <CreateModule /> } loader={ createModuleLoader } />
        <Route path="create-lecturer" element={ <CreateLecturer /> } />
      </Route>
      <Route path="register-student" element={ <RegisterStudent /> } loader={ registerStudentLoader } />
      <Route element={ <RequireAuth /> }>
        <Route path="/student" element={ <StudentRoot /> }>
          <Route index element={ <StudentIndex /> } />
          <Route path="register-modules" element={ <RegisterModules /> } />
          <Route path="results" element={ <ViewResults /> } />
        </Route>
        <Route path="/lecturer" element={ <LecturerRoot /> }>
          <Route index element={ <LecturerIndex /> } />
          <Route path="mark" element={ <LecturerModulesList /> } />
          <Route path="mark/:moduleId" element={ <MarkModule /> } />
          <Route path="course-stats" element={ <CourseResults /> } />
        </Route>
      </Route>
      <Route path="*" element={ <NoMatch /> } />
    </Route>,
  ),
);

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement,
);
root.render(
  <React.StrictMode>
    <NavbarProvider>
      <ConfigProvider>
        <RouterProvider router={ router } />
      </ConfigProvider>
    </NavbarProvider>
  </React.StrictMode>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

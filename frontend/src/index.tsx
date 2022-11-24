import React from 'react';
import ReactDOM from 'react-dom/client';

// LIBRARIES
import { createBrowserRouter, RouterProvider, createRoutesFromElements, Route } from 'react-router-dom';

// COMPONENTS
import { CreateCourse, CreateModule, CreateLecturer, createModuleLoader } from 'features/admin';
import { NavbarProvider } from 'features/navbar';
import { AdminRoot, AdminIndex, ErrorPage, Home, Root, NoMatch } from 'routes';

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
  worker.start();
}

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={ <Root /> } errorElement={ <ErrorPage /> }>
      <Route index element={ <Home /> } />
      <Route path="/admin" element={ <AdminRoot /> }>
        <Route index element={ <AdminIndex /> } />
        <Route path="create-course" element={ <CreateCourse /> } />
        <Route path="create-module" element={ <CreateModule /> } loader={ createModuleLoader } />
        <Route path="create-lecturer" element={ <CreateLecturer /> } />
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
      <RouterProvider router={ router } />
    </NavbarProvider>
  </React.StrictMode>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

import React, { useContext, useLayoutEffect } from 'react';
import { Container } from '@mui/system';
import { NavbarContext } from 'features/navbar';
import { Outlet } from 'react-router-dom';

const studentPages = [
  { name: 'Logout', path: '/logout' },
  { name: 'Register Modules', path: '/student/register-modules' },
  { name: 'View Results', path: '/student/results' },
];

export const StudentRoot = () => {
  const { updatePages } = useContext(NavbarContext);
  useLayoutEffect(() => {
    updatePages(studentPages);
    return () => updatePages([]);
  });
  return (
    <Container>
      <Outlet />
    </Container>
  );
};

export const StudentIndex = () => (
  <Container>
    Student
  </Container>
);

import React, { useContext, useLayoutEffect } from 'react';
import { Container } from '@mui/material';
import { NavbarContext } from 'features/navbar';
import { Outlet } from 'react-router-dom';

const studentPages = [
  { name: 'Logout', path: '/logout' },
  { name: 'Mark Modules', path: '/lecturer/mark' },
];

export const LecturerRoot = () => {
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

export const LecturerIndex = () => (
  <Container>
    Lecturer
  </Container>
);

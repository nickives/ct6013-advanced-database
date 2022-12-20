import React, { useContext, useLayoutEffect } from 'react';
import { Card, CardContent, Container } from '@mui/material';
import { NavbarContext } from 'features/navbar';
import { Outlet } from 'react-router-dom';

const studentPages = [
  { name: 'Logout', path: '/logout' },
  { name: 'Mark Modules', path: '/lecturer/mark' },
  { name: 'Course Stats', path: '/lecturer/course-stats' },
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
    <Card raised className="mt-5">
      <CardContent>
        Select a page from the navbar.
      </CardContent>
    </Card>
  </Container>
);

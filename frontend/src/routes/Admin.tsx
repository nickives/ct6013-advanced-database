import React, { useContext, useLayoutEffect } from 'react';
import { Container } from '@mui/material';
import { NavbarContext } from 'features/navbar';
import { Outlet } from 'react-router-dom';

const adminPages = [
  { name: 'Logout', path: '/logout' },
  { name: 'Create Course', path: '/admin/create-course' },
  { name: 'Create Module', path: '/admin/create-module' },
  { name: 'Create Lecturer', path: '/admin/create-lecturer' },
];

export const AdminRoot = () => {
  const { updatePages } = useContext(NavbarContext);
  useLayoutEffect(() => {
    updatePages(adminPages);
    return () => updatePages([]);
  });
  return (
    <Container>
      <Outlet />
    </Container>
  );
};

export const AdminIndex = () => (
  <Container>
    Admin
  </Container>
);

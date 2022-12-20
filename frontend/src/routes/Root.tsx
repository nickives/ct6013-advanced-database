import React from 'react';
import Container from '@mui/material/Container';
import { Outlet } from 'react-router-dom';
import { Navbar } from 'features/navbar';

const menuItems = [
  { name: 'Admin', path: '/admin' },
  { name: 'Register Student', path: '/register-student' },
];

const Root = () => (
  <>
    <Container>
      <Navbar />
    </Container>
    <Container>
      <Outlet />
    </Container>
  </>
);

export default Root;

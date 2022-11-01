import React, { useContext, useLayoutEffect, useMemo } from 'react';
import Container from '@mui/material/Container';
import { Outlet } from 'react-router-dom';
import { Navbar, NavbarContext, NavbarItem } from 'features/navbar';

const menuItems = [
  { name: 'Admin', path: '/admin' },
  { name: 'Student', path: '/student' },
  { name: 'Lecturer', path: '/lecturer' },
];

const Root = () => (
  <>
    <Container>
      <Navbar
        menuItems={ menuItems }
      />
    </Container>
    <Container>
      <Outlet />
    </Container>
  </>
);

export default Root;

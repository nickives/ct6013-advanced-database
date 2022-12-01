import React, { useContext, useLayoutEffect } from 'react';
import { Container } from '@mui/system';
import { Outlet } from 'react-router-dom';
import { NavbarContext } from 'features/navbar';

// eslint-disable-next-line import/prefer-default-export
export const LoginRoot = () => (
  <Container>
    <Outlet />
  </Container>
);

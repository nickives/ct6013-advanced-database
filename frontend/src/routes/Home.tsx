import { Card, CardContent, Typography } from '@mui/material';
import { NavbarContext, NavbarItem } from 'features/navbar';
import React, { useContext, useLayoutEffect } from 'react';

const emptyPages: NavbarItem[] = [];

const Home = () => {
  const { updatePages } = useContext(NavbarContext);
  useLayoutEffect(() => updatePages(emptyPages));
  return (
    <Card raised className="mt-5">
      <CardContent>
        <Typography
          sx={ {
            fontSize: 16,
          } }
        >
          This is the root page for this application. To switch between user, student, and admin
          views use the menu in the top right corner.
        </Typography>
      </CardContent>
    </Card>
  );
};

export default Home;

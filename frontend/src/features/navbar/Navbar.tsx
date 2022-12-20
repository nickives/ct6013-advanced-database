import React, { useContext } from 'react';
import { AppBar, Box, Button, Container, Toolbar, Typography } from '@mui/material';
import StorageIcon from '@mui/icons-material/Storage';
import { Link } from 'react-router-dom';
import { ConfigContext, DbChoice } from 'features/appconfig/AppConfig';
import { ArrowForward } from '@mui/icons-material';
import { NavbarContext } from './NavbarContext';

const Navbar = (): JSX.Element => {
  const { pages } = useContext(NavbarContext);
  const { dbChoice, setDbChoice, setLoginState } = useContext(ConfigContext);

  const changeDbOnClick = () => {
    setDbChoice(dbChoice === DbChoice.MONGO ? DbChoice.ORACLE : DbChoice.MONGO);
    setLoginState();
    window.location.reload();
  };

  return (
    <AppBar position="static">
      <Container>
        <Toolbar>
          <Link to="/">
            <StorageIcon sx={ { display: { xs: 'flex' }, mr: 1 } } />
          </Link>
          <Typography
            variant="h5"
            noWrap
            component={ Link }
            to="#"
            onClick={ changeDbOnClick }
            sx={ {
              mr: 2,
              display: { xs: 'flex' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            } }
          >
            Change DB <ArrowForward />{ dbChoice === DbChoice.MONGO ? 'MongoDB' : 'Oracle' }
          </Typography>
          <Box sx={ { flexGrow: 1, display: { xs: 'flex' } } }>
            {pages?.map((p) => (
              <Button
                key={ p.path }
                sx={ { my: 2, color: 'white', display: 'block' } }
                component={ Link }
                to={ p.path }
              >
                {p.name}
              </Button>
            ))}
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Navbar;

import React, { useContext } from 'react';
import { AppBar, Box, Button, Container, IconButton, Menu, MenuItem, Toolbar, Typography } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import StorageIcon from '@mui/icons-material/Storage';
import { Link } from 'react-router-dom';
import { NavbarContext } from './NavbarContext';
import { NavbarProps } from './types';

const Navbar = ({ menuItems }: NavbarProps): JSX.Element => {
  const { pages } = useContext(NavbarContext);
  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);

  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  return (
    <AppBar position="static">
      <Container>
        <Toolbar>
          <StorageIcon sx={ { display: { xs: 'flex' }, mr: 1 } } />
          <Typography
            variant="h5"
            noWrap
            component={ Link }
            to="/"
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
            CT6013
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
          <Box>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={ handleOpenNavMenu }
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={ anchorElNav }
              anchorOrigin={ {
                vertical: 'bottom',
                horizontal: 'left',
              } }
              keepMounted
              transformOrigin={ {
                vertical: 'top',
                horizontal: 'left',
              } }
              open={ Boolean(anchorElNav) }
              onClose={ handleCloseNavMenu }
              sx={ {
                display: { xs: 'block' },
              } }
            >
              {menuItems.map((page) => (
                <MenuItem
                  key={ page.path }
                  onClick={ handleCloseNavMenu }
                  component={ Link }
                  to={ page.path }
                >
                  <Typography textAlign="center">{page.name}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Navbar;

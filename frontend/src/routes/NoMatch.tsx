import React from 'react';
import { Card, CardContent, Container, Typography } from '@mui/material';
import { Link } from 'react-router-dom';

const NoMatch = () => (
  <Container
    sx={ { alignSelf: 'center', justifySelf: 'center' } }
  >
    <Card>
      <CardContent sx={ { textAlign: 'center' } }>
        <Typography
          variant="h5"
          noWrap
          sx={ {
            mr: 2,
            flexGrow: 1,
            fontFamily: 'monospace',
            fontWeight: 700,
            fontSize: 96,
            letterSpacing: '.3rem',
            color: 'inherit',
            textDecoration: 'none',
          } }
        >
          Not Found
        </Typography>
        <Typography
          sx={ { fontSize: 96 } }
        >
          😿
        </Typography>
        <Typography
          sx={ { fontSize: 24 } }
        >
          We couldn&apos;t find what you&apos;re looking for. <br />
          Go <Link to="/" className="underline text-blue-600 hover:text-blue-800 visited:text-purple-600">home</Link>?
        </Typography>
      </CardContent>
    </Card>
  </Container>
);

export default NoMatch;

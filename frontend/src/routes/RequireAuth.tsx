import { ConfigContext } from 'features/appconfig/AppConfig';
import { Navigate, Outlet } from 'react-router-dom';
import React from 'react';

// eslint-disable-next-line import/prefer-default-export
export const RequireAuth = () => {
  const { loginState } = React.useContext(ConfigContext);

  if (!loginState) {
    return <Navigate to="/" />;
  }
  return <Outlet />;
};

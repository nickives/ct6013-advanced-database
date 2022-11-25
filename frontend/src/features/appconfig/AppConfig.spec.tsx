import React, { useEffect } from 'react';
import { render, screen } from '@testing-library/react';
import ConfigProvider, { ConfigContext, DbChoice } from './AppConfig';

const TestUpdateComponent = ({ newDbChoice }: { newDbChoice: DbChoice }) => {
  const { dbChoice, setDbChoice } = React.useContext(ConfigContext);
  useEffect(() => setDbChoice(newDbChoice), [newDbChoice, setDbChoice]);
  return <div>{dbChoice}</div>;
};

const TestRenderComponent = () => {
  const { dbChoice } = React.useContext(ConfigContext);
  return <div>{dbChoice}</div>;
};

test('ConfigProvider supplies default value', () => {
  render(
    <ConfigProvider dbChoice={ DbChoice.ORACLE }>
      <TestRenderComponent />
    </ConfigProvider>,
  );
  expect(screen.getByText(DbChoice.ORACLE)).toBeInTheDocument();
});

test('ConfigProvider updates state correctly', () => {
  render(
    <ConfigProvider dbChoice={ DbChoice.ORACLE }>
      <TestUpdateComponent newDbChoice={ DbChoice.MONGO } />
    </ConfigProvider>,
  );
  expect(screen.getByText(DbChoice.MONGO.toString())).toBeInTheDocument();
});

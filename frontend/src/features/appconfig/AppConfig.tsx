/* eslint-disable @typescript-eslint/no-empty-function */
import React, { useState, useMemo, useEffect, useCallback } from 'react';
import { UserType } from 'lib/types';
import axios from 'axios';

export enum DbChoice {
  ORACLE = 'oracle',
  MONGO = 'mongo',
}

interface LoginState {
  userId: string;
  courseId?: string;
  name: string;
  destination: string;
}

interface ConfigContextData {
  dbChoice: DbChoice;
  setDbChoice: (dbChoice: DbChoice) => void;
  loginState?: LoginState;
  setLoginState: (loginState?: LoginState) => void;
}

const initialContextData: ConfigContextData = {
  dbChoice: DbChoice.ORACLE,
  setDbChoice: () => {},
  setLoginState: () => {},
};

export const ConfigContext = React.createContext<ConfigContextData>(initialContextData);

type ConfigProviderProps = React.PropsWithChildren<Partial<ConfigContextData>>;

const ConfigProvider = ({
  children, dbChoice: initialDbChoice, loginState: initialLoginState,
}: ConfigProviderProps) => {
  const dbChoiceLs = localStorage.getItem('dbChoice');
  const loginStateLs = localStorage.getItem('loginState');
  const decodedLoginState = loginStateLs && loginStateLs !== 'undefined'
    ? JSON.parse(loginStateLs)
    : undefined;
  const [dbChoice, _setDbChoice] = useState<DbChoice>(
    initialDbChoice || dbChoiceLs as DbChoice || DbChoice.ORACLE,
  );
  const [
    loginState, _setLoginState,
  ] = useState<LoginState | undefined>(initialLoginState || decodedLoginState);
  const setDbChoice = useCallback((dbChoiceIn: DbChoice) => {
    _setDbChoice(dbChoiceIn);
    localStorage.setItem('dbChoice', dbChoiceIn);
  }, []);

  const setLoginState = useCallback((loginStateIn?: LoginState) => {
    _setLoginState(loginStateIn);
    localStorage.setItem('loginState', JSON.stringify(loginStateIn));
  }, []);

  const contextData: ConfigContextData = useMemo(
    () => ({ dbChoice, setDbChoice, loginState, setLoginState }),
    [dbChoice, loginState, setDbChoice, setLoginState],
  );

  return (
    <ConfigContext.Provider value={ contextData }>
      {children}
    </ConfigContext.Provider>
  );
};

export default ConfigProvider;

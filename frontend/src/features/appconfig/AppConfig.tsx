import React, { useState, useMemo } from 'react';

export enum DbChoice {
  ORACLE = '?db=oracle',
  MONGO = '?db=mongo',
}

interface ConfigContextData {
  dbChoice: DbChoice;
  setDbChoice: (dbChoice: DbChoice) => void;
}

const initialContextData: ConfigContextData = {
  dbChoice: DbChoice.ORACLE,
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  setDbChoice: () => {},
};

export const ConfigContext = React.createContext<ConfigContextData>(initialContextData);

type ConfigProviderProps = React.PropsWithChildren<{
  dbChoice: DbChoice;
}>;

const ConfigProvider = ({ children, dbChoice: initialDbChoice }: ConfigProviderProps) => {
  const [dbChoice, setDbChoice] = useState<DbChoice>(initialDbChoice);

  const contextData: ConfigContextData = useMemo(
    () => ({ dbChoice, setDbChoice }),
    [dbChoice, setDbChoice],
  );

  return (
    <ConfigContext.Provider value={ contextData }>
      {children}
    </ConfigContext.Provider>
  );
};

export default ConfigProvider;

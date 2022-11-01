/* eslint-disable @typescript-eslint/no-empty-function */
import React, { useCallback, useMemo } from 'react';
import { NavbarItem } from './types';

interface NavbarContextData {
  pages: NavbarItem[];
  updatePages: (pages: NavbarItem[]) => void;
}

const initialContextData: NavbarContextData = {
  pages: [],
  updatePages: () => {},
};

export const NavbarContext = React.createContext<NavbarContextData>(initialContextData);

type NavbarProviderProps = React.PropsWithChildren<{
  pages?: NavbarItem[];
}>;

const NavbarProvider = ({ children, pages: initialPages = [] }: NavbarProviderProps) => {
  const [pages, setPages] = React.useState<NavbarItem[]>(initialPages);

  const updatePages = useCallback((newPages: NavbarItem[]) => {
    setPages(newPages);
  }, []);

  const contextData: NavbarContextData = useMemo(
    () => ({ pages, updatePages }),
    [pages, updatePages],
  );

  return (
    <NavbarContext.Provider value={ contextData }>
      {children}
    </NavbarContext.Provider>
  );
};

export default NavbarProvider;

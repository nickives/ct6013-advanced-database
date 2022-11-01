export interface NavbarItem {
  name: string;
  path: string;
}

export interface NavbarProps {
  menuItems: NavbarItem[],
}

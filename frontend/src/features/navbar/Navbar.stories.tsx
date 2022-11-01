/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { withRouter } from 'storybook-addon-react-router-v6';
import { within, userEvent } from '@storybook/testing-library';

import { NavbarProps, Navbar, NavbarProvider } from './index';

export default {
  title: 'Features/Navbar',
  component: Navbar,
  decorators: [
    withRouter,
    (Story) => (
      <NavbarProvider pages={ [
        { name: 'Home', path: '/' },
        { name: 'About', path: '/about' },
        { name: 'Contact', path: '/contact' },
      ] }
      >
        <Story />
      </NavbarProvider>
    ),
  ],
} as ComponentMeta<typeof Navbar>;

const Template: ComponentStory<typeof Navbar> = (args: NavbarProps) => <Navbar { ...args } />;

export const Default = Template.bind({});
Default.args = {
  menuItems: [
    { name: 'Admin', path: '/admin' },
    { name: 'Students', path: '/students' },
    { name: 'Lecturers', path: '/lecturers' },
  ],
};

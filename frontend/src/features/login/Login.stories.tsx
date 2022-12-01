/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import { Story, Meta } from '@storybook/react';
import { userEvent, waitFor, within, screen } from '@storybook/testing-library';
import { expect } from '@storybook/jest';
import { handlers } from 'mocks/handlers';
import { createMemoryRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom';

import Login, { loginLoader } from './Login';

export default {
  title: 'Features/Login',
  component: Login,
  decorators: [(StoryFn) => {
    const router = createMemoryRouter(
      createRoutesFromElements(
        <Route path="/" loader={ loginLoader } element={ <StoryFn /> } />,
      ),
    );
    return (
      <RouterProvider router={ router } />
    );
  }],
  parameters: { msw: { handlers: handlers } },
} as Meta<typeof Login>;

const Template: Story<typeof Login> = () => <Login />;

export const Default = Template.bind({});
Default.play = async ({ canvasElement }) => {};

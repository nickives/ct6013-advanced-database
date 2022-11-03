/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import { Story, Meta } from '@storybook/react';
import { userEvent, waitFor, within, screen } from '@storybook/testing-library';
import { expect } from '@storybook/jest';
import { handlers } from 'mocks/handlers';
import { createMemoryRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom';

// import { withRouter } from 'storybook-addon-react-router-v6';
import CreateModule, { createModuleLoader } from './CreateModule';

export default {
  title: 'Features/Admin/CreateModule',
  component: CreateModule,
  decorators: [(StoryFn) => {
    const router = createMemoryRouter(
      createRoutesFromElements(
        <Route path="/" loader={ createModuleLoader } element={ <StoryFn /> } />,
      ),
    );
    return (
      <RouterProvider router={ router } />
    );
  }],
} as Meta<typeof CreateModule>;

const Template: Story<typeof CreateModule> = () => <CreateModule />;

export const Default = Template.bind({});
Default.parameters = {
  msw: { handlers: handlers },
};

Default.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await waitFor(() => expect(canvas.getByTestId('course-select')).toBeInTheDocument());
  await waitFor(() => expect(canvas.getByTestId('lecturer-select')).toBeInTheDocument());
  await userEvent.type(canvas.getByRole('textbox', { name: /module name/i }), 'Computing');
  // We have to do magic to get the actual UI element 😿
  const courseButton = canvas.getByTestId('course-select').children[0];
  await userEvent.click(courseButton);
  const courseOption = screen.getByRole('option', { name: /course 0/i });
  await userEvent.click(courseOption);

  const lecturerButton = canvas.getByTestId('lecturer-select').children[0];
  await userEvent.click(lecturerButton);
  const lecturerOption = screen.getByRole('option', { name: /john doe 0/i });
  await userEvent.click(lecturerOption);

  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/created module computing/i)).toBeInTheDocument());
};

export const WithServerError = Template.bind({});
WithServerError.parameters = {
  msw: { handlers: handlers },
};
WithServerError.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await waitFor(() => expect(canvas.getByTestId('course-select')).toBeInTheDocument());
  await waitFor(() => expect(canvas.getByTestId('lecturer-select')).toBeInTheDocument());
  await userEvent.type(canvas.getByRole('textbox', { name: /module name/i }), 'errorTest');
  // We have to do magic to get the actual UI element 😿
  const courseButton = canvas.getByTestId('course-select').children[0];
  await userEvent.click(courseButton);
  const courseOption = screen.getByRole('option', { name: /course 0/i });
  await userEvent.click(courseOption);

  const lecturerButton = canvas.getByTestId('lecturer-select').children[0];
  await userEvent.click(lecturerButton);
  const lecturerOption = screen.getByRole('option', { name: /john doe 0/i });
  await userEvent.click(lecturerOption);

  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/server error/i)).toBeInTheDocument());
};

export const WithUniqueError = Template.bind({});
WithUniqueError.parameters = {
  msw: { handlers: handlers },
};
WithUniqueError.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await waitFor(() => expect(canvas.getByTestId('course-select')).toBeInTheDocument());
  await waitFor(() => expect(canvas.getByTestId('lecturer-select')).toBeInTheDocument());
  await userEvent.type(canvas.getByRole('textbox', { name: /module name/i }), 'uniqueErrorTest');
  // We have to do magic to get the actual UI element 😿
  const courseButton = canvas.getByTestId('course-select').children[0];
  await userEvent.click(courseButton);
  const courseOption = screen.getByRole('option', { name: /course 0/i });
  await userEvent.click(courseOption);

  const lecturerButton = canvas.getByTestId('lecturer-select').children[0];
  await userEvent.click(lecturerButton);
  const lecturerOption = screen.getByRole('option', { name: /john doe 0/i });
  await userEvent.click(lecturerOption);

  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/module name already exists/i)).toBeInTheDocument());
};

/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import { Story, Meta } from '@storybook/react';
import { userEvent, waitFor, within, screen } from '@storybook/testing-library';
import { expect } from '@storybook/jest';
import { handlers } from 'mocks/handlers';
import { createMemoryRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom';

import RegisterStudent, { registerStudentLoader } from './RegisterStudent';

export default {
  title: 'Features/Student/RegisterStudent',
  component: RegisterStudent,
  decorators: [(StoryFn) => {
    const router = createMemoryRouter(
      createRoutesFromElements(
        <Route path="/" loader={ registerStudentLoader } element={ <StoryFn /> } />,
      ),
    );
    return (
      <RouterProvider router={ router } />
    );
  }],
  parameters: { msw: { handlers: handlers } },
} as Meta<typeof RegisterStudent>;

const Template: Story<typeof RegisterStudent> = () => <RegisterStudent />;

export const Default = Template.bind({});

export const SuccessfulSubmit = Template.bind({});
SuccessfulSubmit.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await waitFor(() => expect(canvas.getByTestId('course-select')).toBeInTheDocument());
  await userEvent.type(canvas.getByRole('textbox', { name: /first name/i }), 'John');
  await userEvent.type(canvas.getByRole('textbox', { name: /last name/i }), 'Doe');
  const courseButton = canvas.getByTestId('course-select').children[0];
  await userEvent.click(courseButton);
  const courseOption = screen.getByRole('option', { name: /course 0/i });
  await userEvent.click(courseOption);
  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/registered student john doe/i)).toBeInTheDocument());
};

export const WithServerError = Template.bind({});
WithServerError.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await waitFor(() => expect(canvas.getByTestId('course-select')).toBeInTheDocument());
  await userEvent.type(canvas.getByRole('textbox', { name: /first name/i }), 'errorTest');
  await userEvent.type(canvas.getByRole('textbox', { name: /last name/i }), 'Doe');
  const courseButton = canvas.getByTestId('course-select').children[0];
  await userEvent.click(courseButton);
  const courseOption = screen.getByRole('option', { name: /course 0/i });
  await userEvent.click(courseOption);
  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/server error/i)).toBeInTheDocument());
};

export const WithValidationError = Template.bind({});
WithValidationError.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await waitFor(() => expect(canvas.getByTestId('course-select')).toBeInTheDocument());
  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/first name is required/i)).toBeInTheDocument());
  await waitFor(() => expect(canvas.getByText(/last name is required/i)).toBeInTheDocument());
};

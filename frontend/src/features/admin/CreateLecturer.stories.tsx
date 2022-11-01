import React from 'react';
import { Story, Meta } from '@storybook/react';
import { userEvent, waitFor, within } from '@storybook/testing-library';

import { expect } from '@storybook/jest';

import { handlers } from 'mocks/handlers';

import CreateLecturer from './CreateLecturer';

export default {
  title: 'Features/Admin/CreateLecturer',
  component: CreateLecturer,
} as Meta<typeof CreateLecturer>;

const Template: Story<typeof CreateLecturer> = () => <CreateLecturer />;

export const Default = Template.bind({});
Default.parameters = {
  msw: {
    handlers: handlers.slice(0, 1),
  },
};
Default.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await userEvent.type(canvas.getByRole('textbox', { name: /name/i }), 'John');
  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/created lecturer/i)).toBeInTheDocument());
};

export const WithError = Template.bind({});
WithError.parameters = {
  msw: {
    handlers: handlers.slice(0, 2),
  },
};
WithError.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await userEvent.type(canvas.getByRole('textbox', { name: /name/i }), 'errorTest');
  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/server error/i)).toBeInTheDocument());
};

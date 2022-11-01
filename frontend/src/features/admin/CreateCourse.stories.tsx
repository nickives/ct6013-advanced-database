import React from 'react';
import { Story, Meta } from '@storybook/react';
import { userEvent, waitFor, within } from '@storybook/testing-library';

import { expect } from '@storybook/jest';

import { handlers } from 'mocks/handlers';

import CreateCourse from './CreateCourse';

export default {
  title: 'Features/Admin/CreateCourse',
  component: CreateCourse,
} as Meta<typeof CreateCourse>;

const Template: Story<typeof CreateCourse> = () => <CreateCourse />;

export const Default = Template.bind({});
Default.parameters = {
  msw: {
    handlers: handlers.slice(0, 3),
  },
};
Default.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await userEvent.type(canvas.getByRole('textbox', { name: /name/i }), 'Computing');
  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/created course/i)).toBeInTheDocument());
};

export const WithError = Template.bind({});
WithError.parameters = {
  msw: {
    handlers: handlers.slice(0, 3),
  },
};
WithError.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  await userEvent.type(canvas.getByRole('textbox', { name: /name/i }), 'errorTest');
  await userEvent.click(canvas.getByRole('button', { name: /submit/i }));
  await waitFor(() => expect(canvas.getByText(/server error/i)).toBeInTheDocument());
};

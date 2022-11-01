/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import { Story, Meta } from '@storybook/react';
import { within, userEvent } from '@storybook/testing-library';
import { withRouter } from 'storybook-addon-react-router-v6';
import Root from './Root';

export default {
  title: 'Pages/Root',
  component: Root,
  decorators: [withRouter],
} as Meta<typeof Root>;

const Template: Story = () => <Root />;

export const Default = Template.bind({});

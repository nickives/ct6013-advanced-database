/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import { Meta, Story } from '@storybook/react';
import { handlers } from 'mocks/handlers';
import ConfigProvider from 'features/appconfig/AppConfig';

import MarkModule from './MarkModule';

export default {
  title: 'Features/Lecturer/MarkModule',
  component: MarkModule,
  decorators: [(StoryFn) => (
    <ConfigProvider loginState={ {
      userId: '1',
      courseId: undefined,
      name: 'John Doe',
      destination: 'lecturer',
    } }
    >
      <StoryFn />
    </ConfigProvider>
  )],
  parameters: { msw: { handlers: handlers } },
} as Meta<typeof MarkModule>;

const Template: Story<typeof MarkModule> = () => <MarkModule />;

export const Default = Template.bind({});

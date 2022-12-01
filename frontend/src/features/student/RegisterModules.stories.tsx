import React, { useContext, useEffect, useLayoutEffect } from 'react';
import { Story, Meta } from '@storybook/react';
import ConfigProvider, { ConfigContext, DbChoice } from 'features/appconfig/AppConfig';
import { handlers } from 'mocks/handlers';

import RegisterModules from './RegisterModules';

export default {
  title: 'Features/Student/RegisterModules',
  component: RegisterModules,
  decorators: [(StoryFn) => (
    <ConfigProvider loginState={ {
      userId: '1',
      courseId: '1',
      name: 'John Doe',
      destination: 'student',
    } }
    >
      <StoryFn />
    </ConfigProvider>
  )],
  parameters: { msw: { handlers: handlers } },
} as Meta<typeof RegisterModules>;

const Template: Story<typeof RegisterModules> = () => <RegisterModules />;

export const Default = Template.bind({});

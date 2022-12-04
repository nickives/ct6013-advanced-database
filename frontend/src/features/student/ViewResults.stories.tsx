import React from 'react';
import { Story, Meta } from '@storybook/react';
import ConfigProvider from 'features/appconfig/AppConfig';
import { handlers } from 'mocks/handlers';

import ViewResults from './ViewResults';

export default {
  title: 'Features/Student/ViewResults',
  component: ViewResults,
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
} as Meta<typeof ViewResults>;

const Template: Story<typeof ViewResults> = () => <ViewResults />;

export const Default = Template.bind({});

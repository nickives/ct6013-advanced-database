import React from 'react';
import { Story, Meta } from '@storybook/react';
import ConfigProvider from 'features/appconfig/AppConfig';
import { handlers } from 'mocks/handlers';
import { withRouter } from 'storybook-addon-react-router-v6';

import LecturerModulesList from './LecturerModulesList';

export default {
  title: 'Features/Lecturer/LecturerModulesList',
  component: LecturerModulesList,
  decorators: [
    withRouter,
    (StoryFn) => (
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
} as Meta<typeof LecturerModulesList>;

const Template: Story<typeof LecturerModulesList> = () => <LecturerModulesList />;

export const Default = Template.bind({});

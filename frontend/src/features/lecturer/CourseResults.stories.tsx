import React from 'react';
import { Story, Meta } from '@storybook/react';

import CourseResults from './CourseResults';

export default {
  title: 'Features/Lecturer/CourseResults',
  component: CourseResults,
} as Meta<typeof CourseResults>;

const Template: Story<typeof CourseResults> = () => <CourseResults />;

export const Default = Template.bind({});

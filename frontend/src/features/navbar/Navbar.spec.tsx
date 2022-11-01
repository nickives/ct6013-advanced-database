import React from 'react';
import { render, screen } from '@testing-library/react';
import { composeStories } from '@storybook/testing-react';
import * as stories from './Navbar.stories';

const { Default } = composeStories(stories);

test('Renders correctly', () => {
  render(<Default />);
});

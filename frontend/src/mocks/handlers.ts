/* eslint-disable import/no-extraneous-dependencies */
import { rest } from 'msw';

// MOCK DATA
// LECTURERS
const lecturerName = 'John Doe ';
const lecturers: {
  id: string;
  name: string;
}[] = [];
for (let i = 0; i < 100; i += 1) {
  lecturers.push({
    id: i.toString(),
    name: lecturerName + i,
  });
}

// COURSES

// MODULES

// eslint-disable-next-line import/prefer-default-export
export const handlers = [
  // LECTURER HANDLERS
  // Handles a POST create-lecturer lecturer request
  rest.post('/api/create-lecturer', async (req, res, ctx) => { // 1
    const { name }: { name: string } = await req.json();
    if (name === 'errorTest') {
      return res(
        ctx.status(500),
        ctx.json({
          error: 'Internal Server Error',
        }),
      );
    }
    return res(
      ctx.status(201),
      ctx.json({
        id: '1',
        name: name,
      }),
    );
  }),

  // Handles a POST lecturers request
  rest.post('/api/lecturers', async (req, res, ctx) => { // 2
    const { page, limit }: { page: number; limit: number } = await req.json();
    const start = (page - 1) * limit;
    const end = start + limit;
    const data = lecturers.slice(start, end);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a POST create-course request
  rest.post('/api/create-course', async (req, res, ctx) => { // 3
    const { name }: { name: string } = await req.json();
    if (name === 'errorTest') {
      return res(
        ctx.status(500),
        ctx.json({
          error: 'Internal Server Error',
        }),
      );
    }
    return res(
      ctx.status(201),
      ctx.json({
        id: '1',
        name: name,
      }),
    );
  }),
];

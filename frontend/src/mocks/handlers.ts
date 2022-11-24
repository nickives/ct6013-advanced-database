/* eslint-disable import/no-extraneous-dependencies */
import { rest } from 'msw';
import { Course, Lecturer, Module } from 'types';

// MOCK DATA
// LECTURERS
const lecturerName = 'John Doe ';
const lecturers: Lecturer[] = [];
for (let i = 0; i < 100; i += 1) {
  lecturers.push({
    id: i.toString(),
    name: lecturerName + i,
  });
}

// COURSES
const courseName = 'Course ';
const courses: Course[] = [];
for (let i = 0; i < 100; i += 1) {
  courses.push({
    id: i.toString(),
    name: courseName + i,
  });
}

// MODULES
const moduleName = 'Module ';
const modules: Module[] = [];
for (let i = 0; i < 100; i += 1) {
  modules.push({
    id: i.toString(),
    name: moduleName + i,
    courseId: (i + 1).toString(),
    lecturerId: (i + 2).toString(),
  });
}

// eslint-disable-next-line import/prefer-default-export
export const handlers = [
  // LECTURER HANDLERS
  // Handles a POST lecturer request
  rest.post('/api/lecturer', async (req, res, ctx) => { // 1
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

  // Handles a GET lecturer request
  rest.get('/api/lecturer', async (req, res, ctx) => { // 2
    const pageParam = req.url.searchParams.get('page');
    const limitParam = req.url.searchParams.get('limit');
    const page = pageParam ? parseInt(pageParam, 10) : 0;
    const limit = limitParam ? parseInt(limitParam, 10) : lecturers.length;
    const start = page * limit;
    const end = start + limit;
    const data = lecturers.slice(start, end);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a POST course request
  rest.post('/api/course', async (req, res, ctx) => { // 3
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

  // Handles a POST module request
  rest.post('/api/module', async (req, res, ctx) => { // 4
    const { name }: {
      name: string; courseId: string; lecturerId: string;
    } = await req.json();
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

  // Handles a GET course request
  rest.get('/api/course', async (req, res, ctx) => { // 5
    const pageParam = req.url.searchParams.get('page');
    const limitParam = req.url.searchParams.get('limit');
    const page = pageParam ? parseInt(pageParam, 10) : 0;
    const limit = limitParam ? parseInt(limitParam, 10) : courses.length;
    const start = page * limit;
    const end = start + limit;
    const data = courses.slice(start, end);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a POST student request
  rest.post('/api/student', async (req, res, ctx) => { // 6
    const { firstName, lastName } = await req.json();
    if (firstName === 'errorTest') {
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
        firstName: firstName,
        lastName: lastName,
        number: 's1234567',
      }),
    );
  }),
];

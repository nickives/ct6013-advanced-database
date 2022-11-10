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
    const start = page * limit;
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

  // Handles a POST create-module request
  rest.post('/api/create-module', async (req, res, ctx) => { // 4
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

  // Handles a POST courses request
  rest.post('/api/courses', async (req, res, ctx) => { // 5
    const { page, limit }: { page: number; limit: number } = await req.json();
    const start = page * limit;
    const end = start + limit;
    const data = courses.slice(start, end);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a POST create-student request
  rest.post('/api/create-student', async (req, res, ctx) => { // 6
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

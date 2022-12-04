/* eslint-disable import/no-extraneous-dependencies */
import { rest } from 'msw';
import { Course, Lecturer, Module, ModuleMarks, Student, StudentModule } from 'lib/types';

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

// STUDENTS
const studentName = 'Jane Doe ';
const students: Student[] = [];
for (let i = 0; i < 100; i += 1) {
  students.push({
    id: i.toString(),
    name: studentName + i,
    courseId: '1',
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
    code: `CT00${i}`,
    semester: ((i % 2) + 1).toString(),
    catPoints: i % 3 === 0 ? 30 : 15,
    // courseId: (i + 1).toString(),
    lecturerId: (i + 2).toString(),
  });
}

// MODULE RESULTS
const moduleResults: StudentModule[] = [];
for (let i = 0; i < 12; i += 1) {
  moduleResults.push({
    module: modules[i],
    mark: Math.floor(Math.random() * 100),
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

  // Handles a GET student request
  rest.get('/api/student', async (req, res, ctx) => { // 3
    const pageParam = req.url.searchParams.get('page');
    const limitParam = req.url.searchParams.get('limit');
    const page = pageParam ? parseInt(pageParam, 10) : 0;
    const limit = limitParam ? parseInt(limitParam, 10) : students.length;
    const start = page * limit;
    const end = start + limit;
    const data = students.slice(start, end);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a POST course request
  rest.post('/api/course', async (req, res, ctx) => { // 4
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
  rest.post('/api/course/:courseId/modules', async (req, res, ctx) => { // 5
    const { name }: {
      name: string; lecturerId: string;
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
  rest.get('/api/course/:courseId/modules', async (req, res, ctx) => { // 6
    const pageParam = req.url.searchParams.get('page');
    const limitParam = req.url.searchParams.get('limit');
    const page = pageParam ? parseInt(pageParam, 10) : 0;
    const limit = limitParam ? parseInt(limitParam, 10) : courses.length;
    const start = page * limit;
    const end = start + limit;
    const data = modules.slice(start, end);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a GET course request
  rest.get('/api/course', async (req, res, ctx) => { // 6
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
  rest.post('/api/student', async (req, res, ctx) => { // 7
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

  // Handles a POST student module request
  rest.post('/api/student/:studentId/modules', async (req, res, ctx) => { // 7
    const { moduleIds }: { moduleIds: string[] } = await req.json();
    if (moduleIds.length === 3) {
      return res(
        ctx.status(422),
        ctx.json({
          status: 422,
          detail: 'Require 120 CAT points',
          field: 'moduleIds',
        }),
      );
    }
    return res(
      ctx.status(201),
      ctx.json(moduleIds),
    );
  }),

  // Handles a GET student module request
  rest.get('/api/student/:studentId/modules', async (req, res, ctx) => { // 8
    const pageParam = req.url.searchParams.get('page');
    const limitParam = req.url.searchParams.get('limit');
    const page = pageParam ? parseInt(pageParam, 10) : 0;
    const limit = limitParam ? parseInt(limitParam, 10) : courses.length;
    const start = page * limit;
    const end = start + limit;
    const data = moduleResults.slice(start, end);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a GET course request
  rest.get('/api/lecturer/:lecturerId/modules', async (req, res, ctx) => { // 9
    const data = modules.slice(0, 20);
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // Handles a GET course request
  rest.get('/api/lecturer/:lecturerId/modules/:moduleId', async (req, res, ctx) => { // 10
    const data: ModuleMarks = {
      module: modules[0],
      studentMarks: students.slice(0, 80).map((s) => ({
        studentId: s.id,
        firstName: s.name.split(' ')[0],
        lastName: s.name.split(' ')[1],
        mark: Math.floor(Math.random() * 100),
      })),
    };
    return res(
      ctx.status(200),
      ctx.json(data),
    );
  }),
];

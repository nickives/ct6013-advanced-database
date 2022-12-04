export interface Lecturer {
  id: string;
  name: string;
}

export interface Course {
  id: string;
  name: string;
}

export interface Module {
  id: string;
  name: string;
  code: string;
  semester: string;
  catPoints: number;
  lecturerId?: string;
}

export interface Student {
  id: string;
  name: string;
  courseId: string;
}

export enum UserType {
  STUDENT = 'STUDENT',
  LECTURER = 'LECTURER',
  ADMIN = 'ADMIN',
}

export interface StudentREST {
  id: string;
  firstName: string;
  lastName: string;
  modules: string;
  course: string;
}

export interface StudentModule {
  module: Module;
  mark: number;
}

export interface ModuleMarks {
  module: Module;
  studentMarks: {
    studentId: string;
    firstName: string;
    lastName: string;
    mark: number;
  }[]
}

export interface CourseResultREST {
  moduleResults: StudentModule[];
  averageMark: number;
  grade: string;
}

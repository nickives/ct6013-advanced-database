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
  courseId: string;
  lecturerId: string;
}

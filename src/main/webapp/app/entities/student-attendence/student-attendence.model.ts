import * as dayjs from 'dayjs';
import { IClassStudent } from 'app/entities/class-student/class-student.model';

export interface IStudentAttendence {
  id?: number;
  schoolDate?: dayjs.Dayjs;
  attendence?: boolean;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classStudent?: IClassStudent | null;
}

export class StudentAttendence implements IStudentAttendence {
  constructor(
    public id?: number,
    public schoolDate?: dayjs.Dayjs,
    public attendence?: boolean,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classStudent?: IClassStudent | null
  ) {
    this.attendence = this.attendence ?? false;
  }
}

export function getStudentAttendenceIdentifier(studentAttendence: IStudentAttendence): number | undefined {
  return studentAttendence.id;
}

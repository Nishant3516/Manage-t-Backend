import * as dayjs from 'dayjs';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { IClassClassWork } from 'app/entities/class-class-work/class-class-work.model';
import { WorkStatus } from 'app/entities/enumerations/work-status.model';

export interface IStudentClassWorkTrack {
  id?: number;
  workStatus?: WorkStatus;
  remarks?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classStudent?: IClassStudent | null;
  classClassWork?: IClassClassWork | null;
}

export class StudentClassWorkTrack implements IStudentClassWorkTrack {
  constructor(
    public id?: number,
    public workStatus?: WorkStatus,
    public remarks?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classStudent?: IClassStudent | null,
    public classClassWork?: IClassClassWork | null
  ) {}
}

export function getStudentClassWorkTrackIdentifier(studentClassWorkTrack: IStudentClassWorkTrack): number | undefined {
  return studentClassWorkTrack.id;
}

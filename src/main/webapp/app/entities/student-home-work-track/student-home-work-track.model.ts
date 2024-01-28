import * as dayjs from 'dayjs';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { IClassHomeWork } from 'app/entities/class-home-work/class-home-work.model';
import { WorkStatus } from 'app/entities/enumerations/work-status.model';

export interface IStudentHomeWorkTrack {
  id?: number;
  workStatus?: WorkStatus;
  remarks?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classStudent?: IClassStudent | null;
  classHomeWork?: IClassHomeWork | null;
}

export class StudentHomeWorkTrack implements IStudentHomeWorkTrack {
  constructor(
    public id?: number,
    public workStatus?: WorkStatus,
    public remarks?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classStudent?: IClassStudent | null,
    public classHomeWork?: IClassHomeWork | null
  ) {}
}

export function getStudentHomeWorkTrackIdentifier(studentHomeWorkTrack: IStudentHomeWorkTrack): number | undefined {
  return studentHomeWorkTrack.id;
}

import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';

export interface ISchoolEvent {
  id?: number;
  eventName?: string;
  eventDetails?: string | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
}

export class SchoolEvent implements ISchoolEvent {
  constructor(
    public id?: number,
    public eventName?: string,
    public eventDetails?: string | null,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null
  ) {}
}

export function getSchoolEventIdentifier(schoolEvent: ISchoolEvent): number | undefined {
  return schoolEvent.id;
}

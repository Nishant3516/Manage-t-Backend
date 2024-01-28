import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { DayOffType } from 'app/entities/enumerations/day-off-type.model';

export interface ISchoolDaysOff {
  id?: number;
  dayOffType?: DayOffType;
  dayOffName?: string;
  dayOffDetails?: string | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
}

export class SchoolDaysOff implements ISchoolDaysOff {
  constructor(
    public id?: number,
    public dayOffType?: DayOffType,
    public dayOffName?: string,
    public dayOffDetails?: string | null,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null
  ) {}
}

export function getSchoolDaysOffIdentifier(schoolDaysOff: ISchoolDaysOff): number | undefined {
  return schoolDaysOff.id;
}

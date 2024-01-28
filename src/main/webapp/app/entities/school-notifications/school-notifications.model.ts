import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';

export interface ISchoolNotifications {
  id?: number;
  notificationTitle?: string;
  notificationDetails?: string;
  notificationFileContentType?: string | null;
  notificationFile?: string | null;
  notificationFileLink?: string | null;
  showTillDate?: dayjs.Dayjs | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
}

export class SchoolNotifications implements ISchoolNotifications {
  constructor(
    public id?: number,
    public notificationTitle?: string,
    public notificationDetails?: string,
    public notificationFileContentType?: string | null,
    public notificationFile?: string | null,
    public notificationFileLink?: string | null,
    public showTillDate?: dayjs.Dayjs | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null
  ) {}
}

export function getSchoolNotificationsIdentifier(schoolNotifications: ISchoolNotifications): number | undefined {
  return schoolNotifications.id;
}

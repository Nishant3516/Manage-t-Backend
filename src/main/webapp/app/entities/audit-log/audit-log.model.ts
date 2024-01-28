import * as dayjs from 'dayjs';
import { ISchool } from 'app/entities/school/school.model';
import { ISchoolUser } from 'app/entities/school-user/school-user.model';

export interface IAuditLog {
  id?: number;
  userName?: string;
  userDeviceDetails?: string;
  action?: string | null;
  data1?: string | null;
  data2?: string | null;
  data3?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  school?: ISchool | null;
  schoolUser?: ISchoolUser | null;
}

export class AuditLog implements IAuditLog {
  constructor(
    public id?: number,
    public userName?: string,
    public userDeviceDetails?: string,
    public action?: string | null,
    public data1?: string | null,
    public data2?: string | null,
    public data3?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public school?: ISchool | null,
    public schoolUser?: ISchoolUser | null
  ) {}
}

export function getAuditLogIdentifier(auditLog: IAuditLog): number | undefined {
  return auditLog.id;
}

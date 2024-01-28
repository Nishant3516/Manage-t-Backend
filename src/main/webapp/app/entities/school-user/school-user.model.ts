import * as dayjs from 'dayjs';
import { IAuditLog } from 'app/entities/audit-log/audit-log.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { UserType } from 'app/entities/enumerations/user-type.model';

export interface ISchoolUser {
  id?: number;
  loginName?: string;
  password?: string;
  userEmail?: string;
  extraInfo?: string | null;
  activated?: boolean | null;
  userType?: UserType | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  auditLogs?: IAuditLog[] | null;
  schoolClasses?: ISchoolClass[] | null;
  classSubjects?: IClassSubject[] | null;
}

export class SchoolUser implements ISchoolUser {
  constructor(
    public id?: number,
    public loginName?: string,
    public password?: string,
    public userEmail?: string,
    public extraInfo?: string | null,
    public activated?: boolean | null,
    public userType?: UserType | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public auditLogs?: IAuditLog[] | null,
    public schoolClasses?: ISchoolClass[] | null,
    public classSubjects?: IClassSubject[] | null
  ) {
    this.activated = this.activated ?? false;
  }
}

export function getSchoolUserIdentifier(schoolUser: ISchoolUser): number | undefined {
  return schoolUser.id;
}

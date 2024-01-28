import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { IIdStore } from 'app/entities/id-store/id-store.model';
import { IAuditLog } from 'app/entities/audit-log/audit-log.model';

export interface ISchool {
  id?: number;
  groupName?: string;
  schoolName?: string;
  address?: string | null;
  afflNumber?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
  schoolLedgerHeads?: ISchoolLedgerHead[] | null;
  idStores?: IIdStore[] | null;
  auditLogs?: IAuditLog[] | null;
}

export class School implements ISchool {
  constructor(
    public id?: number,
    public groupName?: string,
    public schoolName?: string,
    public address?: string | null,
    public afflNumber?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null,
    public schoolLedgerHeads?: ISchoolLedgerHead[] | null,
    public idStores?: IIdStore[] | null,
    public auditLogs?: IAuditLog[] | null
  ) {}
}

export function getSchoolIdentifier(school: ISchool): number | undefined {
  return school.id;
}

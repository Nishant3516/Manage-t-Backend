import * as dayjs from 'dayjs';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { FeeYear } from 'app/entities/enumerations/fee-year.model';

export interface IStudentAdditionalCharges {
  id?: number;
  feeYear?: FeeYear;
  dueDate?: number;
  janAddChrg?: number | null;
  febAddChrgc?: number | null;
  marAddChrg?: number | null;
  aprAddChrg?: number | null;
  mayAddChrg?: number | null;
  junAddChrg?: number | null;
  julAddChrg?: number | null;
  augAddChrg?: number | null;
  sepAddChrg?: number | null;
  octAddChrg?: number | null;
  novAddChrg?: number | null;
  decAddChrg?: number | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolLedgerHead?: ISchoolLedgerHead | null;
  classStudent?: IClassStudent | null;
}

export class StudentAdditionalCharges implements IStudentAdditionalCharges {
  constructor(
    public id?: number,
    public feeYear?: FeeYear,
    public dueDate?: number,
    public janAddChrg?: number | null,
    public febAddChrgc?: number | null,
    public marAddChrg?: number | null,
    public aprAddChrg?: number | null,
    public mayAddChrg?: number | null,
    public junAddChrg?: number | null,
    public julAddChrg?: number | null,
    public augAddChrg?: number | null,
    public sepAddChrg?: number | null,
    public octAddChrg?: number | null,
    public novAddChrg?: number | null,
    public decAddChrg?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolLedgerHead?: ISchoolLedgerHead | null,
    public classStudent?: IClassStudent | null
  ) {}
}

export function getStudentAdditionalChargesIdentifier(studentAdditionalCharges: IStudentAdditionalCharges): number | undefined {
  return studentAdditionalCharges.id;
}

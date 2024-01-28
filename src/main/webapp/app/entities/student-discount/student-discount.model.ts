import * as dayjs from 'dayjs';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { FeeYear } from 'app/entities/enumerations/fee-year.model';

export interface IStudentDiscount {
  id?: number;
  feeYear?: FeeYear;
  dueDate?: number | null;
  janFeeDisc?: number | null;
  febFeeDisc?: number | null;
  marFeeDisc?: number | null;
  aprFeeDisc?: number | null;
  mayFeeDisc?: number | null;
  junFeeDisc?: number | null;
  julFeeDisc?: number | null;
  augFeeDisc?: number | null;
  sepFeeDisc?: number | null;
  octFeeDisc?: number | null;
  novFeeDisc?: number | null;
  decFeeDisc?: number | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolLedgerHead?: ISchoolLedgerHead | null;
  classStudent?: IClassStudent | null;
}

export class StudentDiscount implements IStudentDiscount {
  constructor(
    public id?: number,
    public feeYear?: FeeYear,
    public dueDate?: number | null,
    public janFeeDisc?: number | null,
    public febFeeDisc?: number | null,
    public marFeeDisc?: number | null,
    public aprFeeDisc?: number | null,
    public mayFeeDisc?: number | null,
    public junFeeDisc?: number | null,
    public julFeeDisc?: number | null,
    public augFeeDisc?: number | null,
    public sepFeeDisc?: number | null,
    public octFeeDisc?: number | null,
    public novFeeDisc?: number | null,
    public decFeeDisc?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolLedgerHead?: ISchoolLedgerHead | null,
    public classStudent?: IClassStudent | null
  ) {}
}

export function getStudentDiscountIdentifier(studentDiscount: IStudentDiscount): number | undefined {
  return studentDiscount.id;
}

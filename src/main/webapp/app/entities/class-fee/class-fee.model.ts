import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { FeeYear } from 'app/entities/enumerations/fee-year.model';

export interface IClassFee {
  id?: number;
  feeYear?: FeeYear;
  dueDate?: number;
  janFee?: number | null;
  febFee?: number | null;
  marFee?: number | null;
  aprFee?: number | null;
  mayFee?: number | null;
  junFee?: number | null;
  julFee?: number | null;
  augFee?: number | null;
  sepFee?: number | null;
  octFee?: number | null;
  novFee?: number | null;
  decFee?: number | null;
  payByDate?: dayjs.Dayjs | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
  schoolLedgerHead?: ISchoolLedgerHead | null;
}

export class ClassFee implements IClassFee {
  constructor(
    public id?: number,
    public feeYear?: FeeYear,
    public dueDate?: number,
    public janFee?: number | null,
    public febFee?: number | null,
    public marFee?: number | null,
    public aprFee?: number | null,
    public mayFee?: number | null,
    public junFee?: number | null,
    public julFee?: number | null,
    public augFee?: number | null,
    public sepFee?: number | null,
    public octFee?: number | null,
    public novFee?: number | null,
    public decFee?: number | null,
    public payByDate?: dayjs.Dayjs | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null,
    public schoolLedgerHead?: ISchoolLedgerHead | null
  ) {}
}

export function getClassFeeIdentifier(classFee: IClassFee): number | undefined {
  return classFee.id;
}

import * as dayjs from 'dayjs';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';

export interface IStudentChargesSummary {
  id?: number;
  summaryType?: string | null;
  feeYear?: string | null;
  dueDate?: number | null;
  aprSummary?: string | null;
  maySummary?: string | null;
  junSummary?: string | null;
  julSummary?: string | null;
  augSummary?: string | null;
  sepSummary?: string | null;
  octSummary?: string | null;
  novSummary?: string | null;
  decSummary?: string | null;
  janSummary?: string | null;
  febSummary?: string | null;
  marSummary?: string | null;
  additionalInfo1?: string | null;
  additionalInfo2?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolLedgerHead?: ISchoolLedgerHead | null;
  classStudent?: IClassStudent | null;
}

export class StudentChargesSummary implements IStudentChargesSummary {
  constructor(
    public id?: number,
    public summaryType?: string | null,
    public feeYear?: string | null,
    public dueDate?: number | null,
    public aprSummary?: string | null,
    public maySummary?: string | null,
    public junSummary?: string | null,
    public julSummary?: string | null,
    public augSummary?: string | null,
    public sepSummary?: string | null,
    public octSummary?: string | null,
    public novSummary?: string | null,
    public decSummary?: string | null,
    public janSummary?: string | null,
    public febSummary?: string | null,
    public marSummary?: string | null,
    public additionalInfo1?: string | null,
    public additionalInfo2?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolLedgerHead?: ISchoolLedgerHead | null,
    public classStudent?: IClassStudent | null
  ) {}
}

export function getStudentChargesSummaryIdentifier(studentChargesSummary: IStudentChargesSummary): number | undefined {
  return studentChargesSummary.id;
}

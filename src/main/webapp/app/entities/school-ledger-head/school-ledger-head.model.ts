import * as dayjs from 'dayjs';
import { IClassFee } from 'app/entities/class-fee/class-fee.model';
import { IStudentDiscount } from 'app/entities/student-discount/student-discount.model';
import { IStudentAdditionalCharges } from 'app/entities/student-additional-charges/student-additional-charges.model';
import { IStudentChargesSummary } from 'app/entities/student-charges-summary/student-charges-summary.model';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolLedgerHeadType } from 'app/entities/enumerations/school-ledger-head-type.model';

export interface ISchoolLedgerHead {
  id?: number;
  studentLedgerHeadType?: SchoolLedgerHeadType;
  ledgerHeadName?: string;
  ledgerHeadLongName?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classFees?: IClassFee[] | null;
  studentDiscounts?: IStudentDiscount[] | null;
  studentAdditionalCharges?: IStudentAdditionalCharges[] | null;
  studentChargesSummaries?: IStudentChargesSummary[] | null;
  school?: ISchool | null;
}

export class SchoolLedgerHead implements ISchoolLedgerHead {
  constructor(
    public id?: number,
    public studentLedgerHeadType?: SchoolLedgerHeadType,
    public ledgerHeadName?: string,
    public ledgerHeadLongName?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classFees?: IClassFee[] | null,
    public studentDiscounts?: IStudentDiscount[] | null,
    public studentAdditionalCharges?: IStudentAdditionalCharges[] | null,
    public studentChargesSummaries?: IStudentChargesSummary[] | null,
    public school?: ISchool | null
  ) {}
}

export function getSchoolLedgerHeadIdentifier(schoolLedgerHead: ISchoolLedgerHead): number | undefined {
  return schoolLedgerHead.id;
}

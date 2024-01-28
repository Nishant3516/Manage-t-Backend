import * as dayjs from 'dayjs';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';

export interface IStudentPayments {
  id?: number;
  amountPaid?: number;
  modeOfPay?: ModeOfPayment | null;
  noteNumbers?: string | null;
  upiId?: string | null;
  remarks?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  receiptId?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classStudent?: IClassStudent | null;
}

export class StudentPayments implements IStudentPayments {
  constructor(
    public id?: number,
    public amountPaid?: number,
    public modeOfPay?: ModeOfPayment | null,
    public noteNumbers?: string | null,
    public upiId?: string | null,
    public remarks?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public receiptId?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classStudent?: IClassStudent | null
  ) {}
}

export function getStudentPaymentsIdentifier(studentPayments: IStudentPayments): number | undefined {
  return studentPayments.id;
}

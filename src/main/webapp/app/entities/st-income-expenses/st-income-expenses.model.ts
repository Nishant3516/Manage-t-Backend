import * as dayjs from 'dayjs';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ISTRoute } from 'app/entities/st-route/st-route.model';
import { IVendors } from 'app/entities/vendors/vendors.model';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

export interface ISTIncomeExpenses {
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
  transactionType?: TransactionType | null;
  classStudent?: IClassStudent | null;
  stRoute?: ISTRoute | null;
  operatedBy?: IVendors | null;
}

export class STIncomeExpenses implements ISTIncomeExpenses {
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
    public transactionType?: TransactionType | null,
    public classStudent?: IClassStudent | null,
    public stRoute?: ISTRoute | null,
    public operatedBy?: IVendors | null
  ) {}
}

export function getSTIncomeExpensesIdentifier(sTIncomeExpenses: ISTIncomeExpenses): number | undefined {
  return sTIncomeExpenses.id;
}

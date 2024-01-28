import * as dayjs from 'dayjs';
import { IVendors } from 'app/entities/vendors/vendors.model';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { ModeOfPayment } from 'app/entities/enumerations/mode-of-payment.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

export interface IIncomeExpenses {
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
  vendor?: IVendors | null;
  ledgerHead?: ISchoolLedgerHead | null;
}

export class IncomeExpenses implements IIncomeExpenses {
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
    public vendor?: IVendors | null,
    public ledgerHead?: ISchoolLedgerHead | null
  ) {}
}

export function getIncomeExpensesIdentifier(incomeExpenses: IIncomeExpenses): number | undefined {
  return incomeExpenses.id;
}

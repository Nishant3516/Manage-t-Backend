import * as dayjs from 'dayjs';
import { ISTIncomeExpenses } from 'app/entities/st-income-expenses/st-income-expenses.model';

export interface ISTRoute {
  id?: number;
  transportRouteName?: string;
  routeCharge?: number;
  transportRouteAddress?: string | null;
  contactNumber?: string | null;
  createDate?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  remarks?: string | null;
  sTIncomeExpenses?: ISTIncomeExpenses[] | null;
}

export class STRoute implements ISTRoute {
  constructor(
    public id?: number,
    public transportRouteName?: string,
    public routeCharge?: number,
    public transportRouteAddress?: string | null,
    public contactNumber?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public remarks?: string | null,
    public sTIncomeExpenses?: ISTIncomeExpenses[] | null
  ) {}
}

export function getSTRouteIdentifier(sTRoute: ISTRoute): number | undefined {
  return sTRoute.id;
}

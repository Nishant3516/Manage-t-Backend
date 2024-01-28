import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { ReportType } from 'app/entities/enumerations/report-type.model';

export interface ISchoolReport {
  id?: number;
  reportType?: ReportType;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
}

export class SchoolReport implements ISchoolReport {
  constructor(
    public id?: number,
    public reportType?: ReportType,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null
  ) {}
}

export function getSchoolReportIdentifier(schoolReport: ISchoolReport): number | undefined {
  return schoolReport.id;
}

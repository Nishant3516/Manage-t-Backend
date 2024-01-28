import * as dayjs from 'dayjs';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { IClassLessionPlan } from 'app/entities/class-lession-plan/class-lession-plan.model';
import { ISchool } from 'app/entities/school/school.model';
import { ISchoolNotifications } from 'app/entities/school-notifications/school-notifications.model';
import { IClassFee } from 'app/entities/class-fee/class-fee.model';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ISchoolUser } from 'app/entities/school-user/school-user.model';
import { ISchoolDaysOff } from 'app/entities/school-days-off/school-days-off.model';
import { ISchoolEvent } from 'app/entities/school-event/school-event.model';
import { ISchoolPictureGallery } from 'app/entities/school-picture-gallery/school-picture-gallery.model';
import { ISchoolVideoGallery } from 'app/entities/school-video-gallery/school-video-gallery.model';
import { ISchoolReport } from 'app/entities/school-report/school-report.model';

export interface ISchoolClass {
  id?: number;
  className?: string;
  classLongName?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classStudents?: IClassStudent[] | null;
  classLessionPlans?: IClassLessionPlan[] | null;
  school?: ISchool | null;
  schoolNotifications?: ISchoolNotifications[] | null;
  classFees?: IClassFee[] | null;
  classSubjects?: IClassSubject[] | null;
  schoolUsers?: ISchoolUser[] | null;
  schoolDaysOffs?: ISchoolDaysOff[] | null;
  schoolEvents?: ISchoolEvent[] | null;
  schoolPictureGalleries?: ISchoolPictureGallery[] | null;
  vchoolVideoGalleries?: ISchoolVideoGallery[] | null;
  schoolReports?: ISchoolReport[] | null;
}

export class SchoolClass implements ISchoolClass {
  constructor(
    public id?: number,
    public className?: string,
    public classLongName?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classStudents?: IClassStudent[] | null,
    public classLessionPlans?: IClassLessionPlan[] | null,
    public school?: ISchool | null,
    public schoolNotifications?: ISchoolNotifications[] | null,
    public classFees?: IClassFee[] | null,
    public classSubjects?: IClassSubject[] | null,
    public schoolUsers?: ISchoolUser[] | null,
    public schoolDaysOffs?: ISchoolDaysOff[] | null,
    public schoolEvents?: ISchoolEvent[] | null,
    public schoolPictureGalleries?: ISchoolPictureGallery[] | null,
    public vchoolVideoGalleries?: ISchoolVideoGallery[] | null,
    public schoolReports?: ISchoolReport[] | null
  ) {}
}

export function getSchoolClassIdentifier(schoolClass: ISchoolClass): number | undefined {
  return schoolClass.id;
}

import * as dayjs from 'dayjs';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { IClassLessionPlan } from 'app/entities/class-lession-plan/class-lession-plan.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { ISchoolUser } from 'app/entities/school-user/school-user.model';

export interface IClassSubject {
  id?: number;
  subjectName?: string;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  subjectChapters?: ISubjectChapter[] | null;
  classLessionPlans?: IClassLessionPlan[] | null;
  schoolClasses?: ISchoolClass[] | null;
  schoolUsers?: ISchoolUser[] | null;
}

export class ClassSubject implements IClassSubject {
  constructor(
    public id?: number,
    public subjectName?: string,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public subjectChapters?: ISubjectChapter[] | null,
    public classLessionPlans?: IClassLessionPlan[] | null,
    public schoolClasses?: ISchoolClass[] | null,
    public schoolUsers?: ISchoolUser[] | null
  ) {}
}

export function getClassSubjectIdentifier(classSubject: IClassSubject): number | undefined {
  return classSubject.id;
}

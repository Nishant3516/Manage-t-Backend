import * as dayjs from 'dayjs';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { IClassLessionPlan } from 'app/entities/class-lession-plan/class-lession-plan.model';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';

export interface ISubjectChapter {
  id?: number;
  chapterName?: string;
  chapterNumber?: number | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  chapterSections?: IChapterSection[] | null;
  classLessionPlans?: IClassLessionPlan[] | null;
  classSubject?: IClassSubject | null;
}

export class SubjectChapter implements ISubjectChapter {
  constructor(
    public id?: number,
    public chapterName?: string,
    public chapterNumber?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public chapterSections?: IChapterSection[] | null,
    public classLessionPlans?: IClassLessionPlan[] | null,
    public classSubject?: IClassSubject | null
  ) {}
}

export function getSubjectChapterIdentifier(subjectChapter: ISubjectChapter): number | undefined {
  return subjectChapter.id;
}

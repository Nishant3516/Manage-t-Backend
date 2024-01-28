import * as dayjs from 'dayjs';
import { IClassHomeWork } from 'app/entities/class-home-work/class-home-work.model';
import { IClassClassWork } from 'app/entities/class-class-work/class-class-work.model';
import { IClassLessionPlan } from 'app/entities/class-lession-plan/class-lession-plan.model';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';

export interface IChapterSection {
  id?: number;
  sectionName?: string;
  sectionNumber?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classHomeWorks?: IClassHomeWork[] | null;
  classClassWorks?: IClassClassWork[] | null;
  classLessionPlans?: IClassLessionPlan[] | null;
  subjectChapter?: ISubjectChapter | null;
}

export class ChapterSection implements IChapterSection {
  constructor(
    public id?: number,
    public sectionName?: string,
    public sectionNumber?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classHomeWorks?: IClassHomeWork[] | null,
    public classClassWorks?: IClassClassWork[] | null,
    public classLessionPlans?: IClassLessionPlan[] | null,
    public subjectChapter?: ISubjectChapter | null
  ) {}
}

export function getChapterSectionIdentifier(chapterSection: IChapterSection): number | undefined {
  return chapterSection.id;
}

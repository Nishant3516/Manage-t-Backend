import * as dayjs from 'dayjs';
import { IClassLessionPlanTrack } from 'app/entities/class-lession-plan-track/class-lession-plan-track.model';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';

export interface IClassLessionPlan {
  id?: number;
  schoolDate?: dayjs.Dayjs;
  classWorkText?: string;
  homeWorkText?: string;
  workStatus?: TaskStatus | null;
  lesionPlanFileContentType?: string | null;
  lesionPlanFile?: string | null;
  lessionPlanFileLink?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  classLessionPlanTracks?: IClassLessionPlanTrack[] | null;
  chapterSection?: IChapterSection | null;
  schoolClass?: ISchoolClass | null;
  classSubject?: IClassSubject | null;
  subjectChapter?: ISubjectChapter | null;
}

export class ClassLessionPlan implements IClassLessionPlan {
  constructor(
    public id?: number,
    public schoolDate?: dayjs.Dayjs,
    public classWorkText?: string,
    public homeWorkText?: string,
    public workStatus?: TaskStatus | null ,
    public lesionPlanFileContentType?: string | null,
    public lesionPlanFile?: string | null,
    public lessionPlanFileLink?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public classLessionPlanTracks?: IClassLessionPlanTrack[] | null,
    public chapterSection?: IChapterSection | null,
    public schoolClass?: ISchoolClass | null,
    public classSubject?: IClassSubject | null,
    public subjectChapter?: ISubjectChapter | null
  ) {}
}

export function getClassLessionPlanIdentifier(classLessionPlan: IClassLessionPlan): number | undefined {
  return classLessionPlan.id;
}

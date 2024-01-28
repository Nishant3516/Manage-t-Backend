import * as dayjs from 'dayjs';
import { IStudentHomeWorkTrack } from 'app/entities/student-home-work-track/student-home-work-track.model';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { StudentAssignmentType } from 'app/entities/enumerations/student-assignment-type.model';

export interface IClassHomeWork {
  id?: number;
  schoolDate?: dayjs.Dayjs;
  studentAssignmentType?: StudentAssignmentType;
  homeWorkText?: string;
  homeWorkFileContentType?: string | null;
  homeWorkFile?: string | null;
  homeWorkFileLink?: string | null;
  assign?: boolean | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  studentHomeWorkTracks?: IStudentHomeWorkTrack[] | null;
  chapterSection?: IChapterSection | null;
}

export class ClassHomeWork implements IClassHomeWork {
  constructor(
    public id?: number,
    public schoolDate?: dayjs.Dayjs,
    public studentAssignmentType?: StudentAssignmentType,
    public homeWorkText?: string,
    public homeWorkFileContentType?: string | null,
    public homeWorkFile?: string | null,
    public homeWorkFileLink?: string | null,
    public assign?: boolean | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public studentHomeWorkTracks?: IStudentHomeWorkTrack[] | null,
    public chapterSection?: IChapterSection | null
  ) {
    this.assign = this.assign ?? false;
  }
}

export function getClassHomeWorkIdentifier(classHomeWork: IClassHomeWork): number | undefined {
  return classHomeWork.id;
}

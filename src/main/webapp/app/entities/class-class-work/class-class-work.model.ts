import * as dayjs from 'dayjs';
import { IStudentClassWorkTrack } from 'app/entities/student-class-work-track/student-class-work-track.model';
import { IChapterSection } from 'app/entities/chapter-section/chapter-section.model';
import { StudentAssignmentType } from 'app/entities/enumerations/student-assignment-type.model';

export interface IClassClassWork {
  id?: number;
  schoolDate?: dayjs.Dayjs;
  studentAssignmentType?: StudentAssignmentType;
  classWorkText?: string;
  classWorkFileContentType?: string | null;
  classWorkFile?: string | null;
  classWorkFileLink?: string | null;
  assign?: boolean | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  studentClassWorkTracks?: IStudentClassWorkTrack[] | null;
  chapterSection?: IChapterSection | null;
}

export class ClassClassWork implements IClassClassWork {
  constructor(
    public id?: number,
    public schoolDate?: dayjs.Dayjs,
    public studentAssignmentType?: StudentAssignmentType,
    public classWorkText?: string,
    public classWorkFileContentType?: string | null,
    public classWorkFile?: string | null,
    public classWorkFileLink?: string | null,
    public assign?: boolean | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public studentClassWorkTracks?: IStudentClassWorkTrack[] | null,
    public chapterSection?: IChapterSection | null
  ) {
    this.assign = this.assign ?? false;
  }
}

export function getClassClassWorkIdentifier(classClassWork: IClassClassWork): number | undefined {
  return classClassWork.id;
}

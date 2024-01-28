import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';

export interface ISchoolVideoGallery {
  id?: number;
  videoTitle?: string;
  videoDescription?: string | null;
  videoFileContentType?: string;
  videoFile?: string;
  videoLink?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
}

export class SchoolVideoGallery implements ISchoolVideoGallery {
  constructor(
    public id?: number,
    public videoTitle?: string,
    public videoDescription?: string | null,
    public videoFileContentType?: string,
    public videoFile?: string,
    public videoLink?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null
  ) {}
}

export function getSchoolVideoGalleryIdentifier(schoolVideoGallery: ISchoolVideoGallery): number | undefined {
  return schoolVideoGallery.id;
}

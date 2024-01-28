import * as dayjs from 'dayjs';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';

export interface ISchoolPictureGallery {
  id?: number;
  pictureTitle?: string;
  pictureDescription?: string | null;
  pictureFileContentType?: string;
  pictureFile?: string;
  pictureLink?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  schoolClasses?: ISchoolClass[] | null;
}

export class SchoolPictureGallery implements ISchoolPictureGallery {
  constructor(
    public id?: number,
    public pictureTitle?: string,
    public pictureDescription?: string | null,
    public pictureFileContentType?: string,
    public pictureFile?: string,
    public pictureLink?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public schoolClasses?: ISchoolClass[] | null
  ) {}
}

export function getSchoolPictureGalleryIdentifier(schoolPictureGallery: ISchoolPictureGallery): number | undefined {
  return schoolPictureGallery.id;
}

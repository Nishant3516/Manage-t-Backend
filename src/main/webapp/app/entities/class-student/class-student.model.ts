import * as dayjs from 'dayjs';
import { IStudentDiscount } from 'app/entities/student-discount/student-discount.model';
import { IStudentAdditionalCharges } from 'app/entities/student-additional-charges/student-additional-charges.model';
import { IStudentChargesSummary } from 'app/entities/student-charges-summary/student-charges-summary.model';
import { IStudentPayments } from 'app/entities/student-payments/student-payments.model';
import { IStudentAttendence } from 'app/entities/student-attendence/student-attendence.model';
import { IStudentHomeWorkTrack } from 'app/entities/student-home-work-track/student-home-work-track.model';
import { IStudentClassWorkTrack } from 'app/entities/student-class-work-track/student-class-work-track.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { BloodGroup } from 'app/entities/enumerations/blood-group.model';

export interface IClassStudent {
  id?: number;
  studentPhotoContentType?: string | null;
  studentPhoto?: string | null;
  studentPhotoLink?: string | null;
  studentId?: string | null;
  firstName?: string;
  gender?: Gender | null;
  lastName?: string | null;
  rollNumber?: string | null;
  phoneNumber?: string | null;
  bloodGroup?: BloodGroup | null;
  dateOfBirth?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  nickName?: string | null;
  fatherName?: string | null;
  motherName?: string | null;
  email?: string | null;
  admissionDate?: dayjs.Dayjs | null;
  regNumber?: string | null;
  endDate?: dayjs.Dayjs | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  studentDiscounts?: IStudentDiscount[] | null;
  studentAdditionalCharges?: IStudentAdditionalCharges[] | null;
  studentChargesSummaries?: IStudentChargesSummary[] | null;
  studentPayments?: IStudentPayments[] | null;
  studentAttendences?: IStudentAttendence[] | null;
  studentHomeWorkTracks?: IStudentHomeWorkTrack[] | null;
  studentClassWorkTracks?: IStudentClassWorkTrack[] | null;
  schoolClass?: ISchoolClass | null;
}

export class ClassStudent implements IClassStudent {
  constructor(
    public id?: number,
    public studentPhotoContentType?: string | null,
    public studentPhoto?: string | null,
    public studentPhotoLink?: string | null,
    public studentId?: string | null,
    public firstName?: string,
    public gender?: Gender | null,
    public lastName?: string | null,
    public rollNumber?: string | null,
    public phoneNumber?: string | null,
    public bloodGroup?: BloodGroup | null,
    public dateOfBirth?: dayjs.Dayjs | null,
    public startDate?: dayjs.Dayjs | null,
    public addressLine1?: string | null,
    public addressLine2?: string | null,
    public nickName?: string | null,
    public fatherName?: string | null,
    public motherName?: string | null,
    public email?: string | null,
    public admissionDate?: dayjs.Dayjs | null,
    public regNumber?: string | null,
    public endDate?: dayjs.Dayjs | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public studentDiscounts?: IStudentDiscount[] | null,
    public studentAdditionalCharges?: IStudentAdditionalCharges[] | null,
    public studentChargesSummaries?: IStudentChargesSummary[] | null,
    public studentPayments?: IStudentPayments[] | null,
    public studentAttendences?: IStudentAttendence[] | null,
    public studentHomeWorkTracks?: IStudentHomeWorkTrack[] | null,
    public studentClassWorkTracks?: IStudentClassWorkTrack[] | null,
    public schoolClass?: ISchoolClass | null
  ) {}
}

export function getClassStudentIdentifier(classStudent: IClassStudent): number | undefined {
  return classStudent.id;
}

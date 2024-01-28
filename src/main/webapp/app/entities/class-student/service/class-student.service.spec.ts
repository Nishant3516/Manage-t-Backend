import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { BloodGroup } from 'app/entities/enumerations/blood-group.model';
import { IClassStudent, ClassStudent } from '../class-student.model';

import { ClassStudentService } from './class-student.service';

describe('Service Tests', () => {
  describe('ClassStudent Service', () => {
    let service: ClassStudentService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassStudent;
    let expectedResult: IClassStudent | IClassStudent[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassStudentService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        studentPhotoContentType: 'image/png',
        studentPhoto: 'AAAAAAA',
        studentPhotoLink: 'AAAAAAA',
        studentId: 'AAAAAAA',
        firstName: 'AAAAAAA',
        gender: Gender.FEMALE,
        lastName: 'AAAAAAA',
        rollNumber: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        bloodGroup: BloodGroup.A_Pos,
        dateOfBirth: currentDate,
        startDate: currentDate,
        addressLine1: 'AAAAAAA',
        addressLine2: 'AAAAAAA',
        nickName: 'AAAAAAA',
        fatherName: 'AAAAAAA',
        motherName: 'AAAAAAA',
        email: 'AAAAAAA',
        admissionDate: currentDate,
        regNumber: 'AAAAAAA',
        endDate: currentDate,
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            admissionDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ClassStudent', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            admissionDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
            startDate: currentDate,
            admissionDate: currentDate,
            endDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ClassStudent()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassStudent', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            studentPhoto: 'BBBBBB',
            studentPhotoLink: 'BBBBBB',
            studentId: 'BBBBBB',
            firstName: 'BBBBBB',
            gender: 'BBBBBB',
            lastName: 'BBBBBB',
            rollNumber: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            bloodGroup: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            nickName: 'BBBBBB',
            fatherName: 'BBBBBB',
            motherName: 'BBBBBB',
            email: 'BBBBBB',
            admissionDate: currentDate.format(DATE_FORMAT),
            regNumber: 'BBBBBB',
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
            startDate: currentDate,
            admissionDate: currentDate,
            endDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ClassStudent', () => {
        const patchObject = Object.assign(
          {
            studentPhoto: 'BBBBBB',
            firstName: 'BBBBBB',
            gender: 'BBBBBB',
            bloodGroup: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
            email: 'BBBBBB',
            admissionDate: currentDate.format(DATE_FORMAT),
            regNumber: 'BBBBBB',
            endDate: currentDate.format(DATE_FORMAT),
          },
          new ClassStudent()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
            startDate: currentDate,
            admissionDate: currentDate,
            endDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ClassStudent', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            studentPhoto: 'BBBBBB',
            studentPhotoLink: 'BBBBBB',
            studentId: 'BBBBBB',
            firstName: 'BBBBBB',
            gender: 'BBBBBB',
            lastName: 'BBBBBB',
            rollNumber: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            bloodGroup: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            nickName: 'BBBBBB',
            fatherName: 'BBBBBB',
            motherName: 'BBBBBB',
            email: 'BBBBBB',
            admissionDate: currentDate.format(DATE_FORMAT),
            regNumber: 'BBBBBB',
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
            startDate: currentDate,
            admissionDate: currentDate,
            endDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ClassStudent', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassStudentToCollectionIfMissing', () => {
        it('should add a ClassStudent to an empty array', () => {
          const classStudent: IClassStudent = { id: 123 };
          expectedResult = service.addClassStudentToCollectionIfMissing([], classStudent);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classStudent);
        });

        it('should not add a ClassStudent to an array that contains it', () => {
          const classStudent: IClassStudent = { id: 123 };
          const classStudentCollection: IClassStudent[] = [
            {
              ...classStudent,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassStudentToCollectionIfMissing(classStudentCollection, classStudent);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassStudent to an array that doesn't contain it", () => {
          const classStudent: IClassStudent = { id: 123 };
          const classStudentCollection: IClassStudent[] = [{ id: 456 }];
          expectedResult = service.addClassStudentToCollectionIfMissing(classStudentCollection, classStudent);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classStudent);
        });

        it('should add only unique ClassStudent to an array', () => {
          const classStudentArray: IClassStudent[] = [{ id: 123 }, { id: 456 }, { id: 25943 }];
          const classStudentCollection: IClassStudent[] = [{ id: 123 }];
          expectedResult = service.addClassStudentToCollectionIfMissing(classStudentCollection, ...classStudentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classStudent: IClassStudent = { id: 123 };
          const classStudent2: IClassStudent = { id: 456 };
          expectedResult = service.addClassStudentToCollectionIfMissing([], classStudent, classStudent2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classStudent);
          expect(expectedResult).toContain(classStudent2);
        });

        it('should accept null and undefined values', () => {
          const classStudent: IClassStudent = { id: 123 };
          expectedResult = service.addClassStudentToCollectionIfMissing([], null, classStudent, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classStudent);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

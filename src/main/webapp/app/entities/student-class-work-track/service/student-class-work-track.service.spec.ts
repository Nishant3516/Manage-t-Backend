import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { WorkStatus } from 'app/entities/enumerations/work-status.model';
import { IStudentClassWorkTrack, StudentClassWorkTrack } from '../student-class-work-track.model';

import { StudentClassWorkTrackService } from './student-class-work-track.service';

describe('Service Tests', () => {
  describe('StudentClassWorkTrack Service', () => {
    let service: StudentClassWorkTrackService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentClassWorkTrack;
    let expectedResult: IStudentClassWorkTrack | IStudentClassWorkTrack[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentClassWorkTrackService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        workStatus: WorkStatus.Done,
        remarks: 'AAAAAAA',
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
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

      it('should create a StudentClassWorkTrack', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new StudentClassWorkTrack()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentClassWorkTrack', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            workStatus: 'BBBBBB',
            remarks: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should partial update a StudentClassWorkTrack', () => {
        const patchObject = Object.assign(
          {
            remarks: 'BBBBBB',
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new StudentClassWorkTrack()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
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

      it('should return a list of StudentClassWorkTrack', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            workStatus: 'BBBBBB',
            remarks: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a StudentClassWorkTrack', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentClassWorkTrackToCollectionIfMissing', () => {
        it('should add a StudentClassWorkTrack to an empty array', () => {
          const studentClassWorkTrack: IStudentClassWorkTrack = { id: 123 };
          expectedResult = service.addStudentClassWorkTrackToCollectionIfMissing([], studentClassWorkTrack);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentClassWorkTrack);
        });

        it('should not add a StudentClassWorkTrack to an array that contains it', () => {
          const studentClassWorkTrack: IStudentClassWorkTrack = { id: 123 };
          const studentClassWorkTrackCollection: IStudentClassWorkTrack[] = [
            {
              ...studentClassWorkTrack,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentClassWorkTrackToCollectionIfMissing(studentClassWorkTrackCollection, studentClassWorkTrack);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentClassWorkTrack to an array that doesn't contain it", () => {
          const studentClassWorkTrack: IStudentClassWorkTrack = { id: 123 };
          const studentClassWorkTrackCollection: IStudentClassWorkTrack[] = [{ id: 456 }];
          expectedResult = service.addStudentClassWorkTrackToCollectionIfMissing(studentClassWorkTrackCollection, studentClassWorkTrack);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentClassWorkTrack);
        });

        it('should add only unique StudentClassWorkTrack to an array', () => {
          const studentClassWorkTrackArray: IStudentClassWorkTrack[] = [{ id: 123 }, { id: 456 }, { id: 7281 }];
          const studentClassWorkTrackCollection: IStudentClassWorkTrack[] = [{ id: 123 }];
          expectedResult = service.addStudentClassWorkTrackToCollectionIfMissing(
            studentClassWorkTrackCollection,
            ...studentClassWorkTrackArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentClassWorkTrack: IStudentClassWorkTrack = { id: 123 };
          const studentClassWorkTrack2: IStudentClassWorkTrack = { id: 456 };
          expectedResult = service.addStudentClassWorkTrackToCollectionIfMissing([], studentClassWorkTrack, studentClassWorkTrack2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentClassWorkTrack);
          expect(expectedResult).toContain(studentClassWorkTrack2);
        });

        it('should accept null and undefined values', () => {
          const studentClassWorkTrack: IStudentClassWorkTrack = { id: 123 };
          expectedResult = service.addStudentClassWorkTrackToCollectionIfMissing([], null, studentClassWorkTrack, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentClassWorkTrack);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

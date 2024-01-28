import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { WorkStatus } from 'app/entities/enumerations/work-status.model';
import { IStudentHomeWorkTrack, StudentHomeWorkTrack } from '../student-home-work-track.model';

import { StudentHomeWorkTrackService } from './student-home-work-track.service';

describe('Service Tests', () => {
  describe('StudentHomeWorkTrack Service', () => {
    let service: StudentHomeWorkTrackService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentHomeWorkTrack;
    let expectedResult: IStudentHomeWorkTrack | IStudentHomeWorkTrack[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StudentHomeWorkTrackService);
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

      it('should create a StudentHomeWorkTrack', () => {
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

        service.create(new StudentHomeWorkTrack()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentHomeWorkTrack', () => {
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

      it('should partial update a StudentHomeWorkTrack', () => {
        const patchObject = Object.assign(
          {
            workStatus: 'BBBBBB',
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new StudentHomeWorkTrack()
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

      it('should return a list of StudentHomeWorkTrack', () => {
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

      it('should delete a StudentHomeWorkTrack', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStudentHomeWorkTrackToCollectionIfMissing', () => {
        it('should add a StudentHomeWorkTrack to an empty array', () => {
          const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 123 };
          expectedResult = service.addStudentHomeWorkTrackToCollectionIfMissing([], studentHomeWorkTrack);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentHomeWorkTrack);
        });

        it('should not add a StudentHomeWorkTrack to an array that contains it', () => {
          const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 123 };
          const studentHomeWorkTrackCollection: IStudentHomeWorkTrack[] = [
            {
              ...studentHomeWorkTrack,
            },
            { id: 456 },
          ];
          expectedResult = service.addStudentHomeWorkTrackToCollectionIfMissing(studentHomeWorkTrackCollection, studentHomeWorkTrack);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StudentHomeWorkTrack to an array that doesn't contain it", () => {
          const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 123 };
          const studentHomeWorkTrackCollection: IStudentHomeWorkTrack[] = [{ id: 456 }];
          expectedResult = service.addStudentHomeWorkTrackToCollectionIfMissing(studentHomeWorkTrackCollection, studentHomeWorkTrack);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentHomeWorkTrack);
        });

        it('should add only unique StudentHomeWorkTrack to an array', () => {
          const studentHomeWorkTrackArray: IStudentHomeWorkTrack[] = [{ id: 123 }, { id: 456 }, { id: 91211 }];
          const studentHomeWorkTrackCollection: IStudentHomeWorkTrack[] = [{ id: 123 }];
          expectedResult = service.addStudentHomeWorkTrackToCollectionIfMissing(
            studentHomeWorkTrackCollection,
            ...studentHomeWorkTrackArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 123 };
          const studentHomeWorkTrack2: IStudentHomeWorkTrack = { id: 456 };
          expectedResult = service.addStudentHomeWorkTrackToCollectionIfMissing([], studentHomeWorkTrack, studentHomeWorkTrack2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(studentHomeWorkTrack);
          expect(expectedResult).toContain(studentHomeWorkTrack2);
        });

        it('should accept null and undefined values', () => {
          const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 123 };
          expectedResult = service.addStudentHomeWorkTrackToCollectionIfMissing([], null, studentHomeWorkTrack, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(studentHomeWorkTrack);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

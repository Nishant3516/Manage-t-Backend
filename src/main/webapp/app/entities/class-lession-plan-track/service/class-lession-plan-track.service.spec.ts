import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';
import { IClassLessionPlanTrack, ClassLessionPlanTrack } from '../class-lession-plan-track.model';

import { ClassLessionPlanTrackService } from './class-lession-plan-track.service';

describe('Service Tests', () => {
  describe('ClassLessionPlanTrack Service', () => {
    let service: ClassLessionPlanTrackService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassLessionPlanTrack;
    let expectedResult: IClassLessionPlanTrack | IClassLessionPlanTrack[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassLessionPlanTrackService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        workStatus: TaskStatus.NotStarted,
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

      it('should create a ClassLessionPlanTrack', () => {
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

        service.create(new ClassLessionPlanTrack()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassLessionPlanTrack', () => {
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

      it('should partial update a ClassLessionPlanTrack', () => {
        const patchObject = Object.assign(
          {
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new ClassLessionPlanTrack()
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

      it('should return a list of ClassLessionPlanTrack', () => {
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

      it('should delete a ClassLessionPlanTrack', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassLessionPlanTrackToCollectionIfMissing', () => {
        it('should add a ClassLessionPlanTrack to an empty array', () => {
          const classLessionPlanTrack: IClassLessionPlanTrack = { id: 123 };
          expectedResult = service.addClassLessionPlanTrackToCollectionIfMissing([], classLessionPlanTrack);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classLessionPlanTrack);
        });

        it('should not add a ClassLessionPlanTrack to an array that contains it', () => {
          const classLessionPlanTrack: IClassLessionPlanTrack = { id: 123 };
          const classLessionPlanTrackCollection: IClassLessionPlanTrack[] = [
            {
              ...classLessionPlanTrack,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassLessionPlanTrackToCollectionIfMissing(classLessionPlanTrackCollection, classLessionPlanTrack);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassLessionPlanTrack to an array that doesn't contain it", () => {
          const classLessionPlanTrack: IClassLessionPlanTrack = { id: 123 };
          const classLessionPlanTrackCollection: IClassLessionPlanTrack[] = [{ id: 456 }];
          expectedResult = service.addClassLessionPlanTrackToCollectionIfMissing(classLessionPlanTrackCollection, classLessionPlanTrack);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classLessionPlanTrack);
        });

        it('should add only unique ClassLessionPlanTrack to an array', () => {
          const classLessionPlanTrackArray: IClassLessionPlanTrack[] = [{ id: 123 }, { id: 456 }, { id: 952 }];
          const classLessionPlanTrackCollection: IClassLessionPlanTrack[] = [{ id: 123 }];
          expectedResult = service.addClassLessionPlanTrackToCollectionIfMissing(
            classLessionPlanTrackCollection,
            ...classLessionPlanTrackArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classLessionPlanTrack: IClassLessionPlanTrack = { id: 123 };
          const classLessionPlanTrack2: IClassLessionPlanTrack = { id: 456 };
          expectedResult = service.addClassLessionPlanTrackToCollectionIfMissing([], classLessionPlanTrack, classLessionPlanTrack2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classLessionPlanTrack);
          expect(expectedResult).toContain(classLessionPlanTrack2);
        });

        it('should accept null and undefined values', () => {
          const classLessionPlanTrack: IClassLessionPlanTrack = { id: 123 };
          expectedResult = service.addClassLessionPlanTrackToCollectionIfMissing([], null, classLessionPlanTrack, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classLessionPlanTrack);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

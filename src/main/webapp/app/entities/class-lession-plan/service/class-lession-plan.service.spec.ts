import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';
import { IClassLessionPlan, ClassLessionPlan } from '../class-lession-plan.model';

import { ClassLessionPlanService } from './class-lession-plan.service';

describe('Service Tests', () => {
  describe('ClassLessionPlan Service', () => {
    let service: ClassLessionPlanService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassLessionPlan;
    let expectedResult: IClassLessionPlan | IClassLessionPlan[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassLessionPlanService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        schoolDate: currentDate,
        classWorkText: 'AAAAAAA',
        homeWorkText: 'AAAAAAA',
        workStatus: TaskStatus.NotStarted,
        lesionPlanFileContentType: 'image/png',
        lesionPlanFile: 'AAAAAAA',
        lessionPlanFileLink: 'AAAAAAA',
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            schoolDate: currentDate.format(DATE_FORMAT),
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

      it('should create a ClassLessionPlan', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            schoolDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ClassLessionPlan()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassLessionPlan', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            classWorkText: 'BBBBBB',
            homeWorkText: 'BBBBBB',
            workStatus: 'BBBBBB',
            lesionPlanFile: 'BBBBBB',
            lessionPlanFileLink: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should partial update a ClassLessionPlan', () => {
        const patchObject = Object.assign(
          {
            schoolDate: currentDate.format(DATE_FORMAT),
            homeWorkText: 'BBBBBB',
            workStatus: 'BBBBBB',
            lesionPlanFile: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
          },
          new ClassLessionPlan()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should return a list of ClassLessionPlan', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            schoolDate: currentDate.format(DATE_FORMAT),
            classWorkText: 'BBBBBB',
            homeWorkText: 'BBBBBB',
            workStatus: 'BBBBBB',
            lesionPlanFile: 'BBBBBB',
            lessionPlanFileLink: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            schoolDate: currentDate,
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

      it('should delete a ClassLessionPlan', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassLessionPlanToCollectionIfMissing', () => {
        it('should add a ClassLessionPlan to an empty array', () => {
          const classLessionPlan: IClassLessionPlan = { id: 123 };
          expectedResult = service.addClassLessionPlanToCollectionIfMissing([], classLessionPlan);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classLessionPlan);
        });

        it('should not add a ClassLessionPlan to an array that contains it', () => {
          const classLessionPlan: IClassLessionPlan = { id: 123 };
          const classLessionPlanCollection: IClassLessionPlan[] = [
            {
              ...classLessionPlan,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassLessionPlanToCollectionIfMissing(classLessionPlanCollection, classLessionPlan);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassLessionPlan to an array that doesn't contain it", () => {
          const classLessionPlan: IClassLessionPlan = { id: 123 };
          const classLessionPlanCollection: IClassLessionPlan[] = [{ id: 456 }];
          expectedResult = service.addClassLessionPlanToCollectionIfMissing(classLessionPlanCollection, classLessionPlan);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classLessionPlan);
        });

        it('should add only unique ClassLessionPlan to an array', () => {
          const classLessionPlanArray: IClassLessionPlan[] = [{ id: 123 }, { id: 456 }, { id: 10690 }];
          const classLessionPlanCollection: IClassLessionPlan[] = [{ id: 123 }];
          expectedResult = service.addClassLessionPlanToCollectionIfMissing(classLessionPlanCollection, ...classLessionPlanArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classLessionPlan: IClassLessionPlan = { id: 123 };
          const classLessionPlan2: IClassLessionPlan = { id: 456 };
          expectedResult = service.addClassLessionPlanToCollectionIfMissing([], classLessionPlan, classLessionPlan2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classLessionPlan);
          expect(expectedResult).toContain(classLessionPlan2);
        });

        it('should accept null and undefined values', () => {
          const classLessionPlan: IClassLessionPlan = { id: 123 };
          expectedResult = service.addClassLessionPlanToCollectionIfMissing([], null, classLessionPlan, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classLessionPlan);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

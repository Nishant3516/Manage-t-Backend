import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { DayOffType } from 'app/entities/enumerations/day-off-type.model';
import { ISchoolDaysOff, SchoolDaysOff } from '../school-days-off.model';

import { SchoolDaysOffService } from './school-days-off.service';

describe('Service Tests', () => {
  describe('SchoolDaysOff Service', () => {
    let service: SchoolDaysOffService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolDaysOff;
    let expectedResult: ISchoolDaysOff | ISchoolDaysOff[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolDaysOffService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dayOffType: DayOffType.WEEKEND,
        dayOffName: 'AAAAAAA',
        dayOffDetails: 'AAAAAAA',
        startDate: currentDate,
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
            startDate: currentDate.format(DATE_FORMAT),
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

      it('should create a SchoolDaysOff', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SchoolDaysOff()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolDaysOff', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dayOffType: 'BBBBBB',
            dayOffName: 'BBBBBB',
            dayOffDetails: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
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

      it('should partial update a SchoolDaysOff', () => {
        const patchObject = Object.assign(
          {
            dayOffName: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new SchoolDaysOff()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
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

      it('should return a list of SchoolDaysOff', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dayOffType: 'BBBBBB',
            dayOffName: 'BBBBBB',
            dayOffDetails: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
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

      it('should delete a SchoolDaysOff', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolDaysOffToCollectionIfMissing', () => {
        it('should add a SchoolDaysOff to an empty array', () => {
          const schoolDaysOff: ISchoolDaysOff = { id: 123 };
          expectedResult = service.addSchoolDaysOffToCollectionIfMissing([], schoolDaysOff);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolDaysOff);
        });

        it('should not add a SchoolDaysOff to an array that contains it', () => {
          const schoolDaysOff: ISchoolDaysOff = { id: 123 };
          const schoolDaysOffCollection: ISchoolDaysOff[] = [
            {
              ...schoolDaysOff,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolDaysOffToCollectionIfMissing(schoolDaysOffCollection, schoolDaysOff);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolDaysOff to an array that doesn't contain it", () => {
          const schoolDaysOff: ISchoolDaysOff = { id: 123 };
          const schoolDaysOffCollection: ISchoolDaysOff[] = [{ id: 456 }];
          expectedResult = service.addSchoolDaysOffToCollectionIfMissing(schoolDaysOffCollection, schoolDaysOff);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolDaysOff);
        });

        it('should add only unique SchoolDaysOff to an array', () => {
          const schoolDaysOffArray: ISchoolDaysOff[] = [{ id: 123 }, { id: 456 }, { id: 90163 }];
          const schoolDaysOffCollection: ISchoolDaysOff[] = [{ id: 123 }];
          expectedResult = service.addSchoolDaysOffToCollectionIfMissing(schoolDaysOffCollection, ...schoolDaysOffArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolDaysOff: ISchoolDaysOff = { id: 123 };
          const schoolDaysOff2: ISchoolDaysOff = { id: 456 };
          expectedResult = service.addSchoolDaysOffToCollectionIfMissing([], schoolDaysOff, schoolDaysOff2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolDaysOff);
          expect(expectedResult).toContain(schoolDaysOff2);
        });

        it('should accept null and undefined values', () => {
          const schoolDaysOff: ISchoolDaysOff = { id: 123 };
          expectedResult = service.addSchoolDaysOffToCollectionIfMissing([], null, schoolDaysOff, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolDaysOff);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

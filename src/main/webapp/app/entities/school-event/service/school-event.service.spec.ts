import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISchoolEvent, SchoolEvent } from '../school-event.model';

import { SchoolEventService } from './school-event.service';

describe('Service Tests', () => {
  describe('SchoolEvent Service', () => {
    let service: SchoolEventService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolEvent;
    let expectedResult: ISchoolEvent | ISchoolEvent[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolEventService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        eventName: 'AAAAAAA',
        eventDetails: 'AAAAAAA',
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

      it('should create a SchoolEvent', () => {
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

        service.create(new SchoolEvent()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolEvent', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            eventName: 'BBBBBB',
            eventDetails: 'BBBBBB',
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

      it('should partial update a SchoolEvent', () => {
        const patchObject = Object.assign(
          {
            eventDetails: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
          },
          new SchoolEvent()
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

      it('should return a list of SchoolEvent', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            eventName: 'BBBBBB',
            eventDetails: 'BBBBBB',
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

      it('should delete a SchoolEvent', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolEventToCollectionIfMissing', () => {
        it('should add a SchoolEvent to an empty array', () => {
          const schoolEvent: ISchoolEvent = { id: 123 };
          expectedResult = service.addSchoolEventToCollectionIfMissing([], schoolEvent);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolEvent);
        });

        it('should not add a SchoolEvent to an array that contains it', () => {
          const schoolEvent: ISchoolEvent = { id: 123 };
          const schoolEventCollection: ISchoolEvent[] = [
            {
              ...schoolEvent,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolEventToCollectionIfMissing(schoolEventCollection, schoolEvent);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolEvent to an array that doesn't contain it", () => {
          const schoolEvent: ISchoolEvent = { id: 123 };
          const schoolEventCollection: ISchoolEvent[] = [{ id: 456 }];
          expectedResult = service.addSchoolEventToCollectionIfMissing(schoolEventCollection, schoolEvent);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolEvent);
        });

        it('should add only unique SchoolEvent to an array', () => {
          const schoolEventArray: ISchoolEvent[] = [{ id: 123 }, { id: 456 }, { id: 71168 }];
          const schoolEventCollection: ISchoolEvent[] = [{ id: 123 }];
          expectedResult = service.addSchoolEventToCollectionIfMissing(schoolEventCollection, ...schoolEventArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolEvent: ISchoolEvent = { id: 123 };
          const schoolEvent2: ISchoolEvent = { id: 456 };
          expectedResult = service.addSchoolEventToCollectionIfMissing([], schoolEvent, schoolEvent2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolEvent);
          expect(expectedResult).toContain(schoolEvent2);
        });

        it('should accept null and undefined values', () => {
          const schoolEvent: ISchoolEvent = { id: 123 };
          expectedResult = service.addSchoolEventToCollectionIfMissing([], null, schoolEvent, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolEvent);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

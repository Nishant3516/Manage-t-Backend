import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISchoolNotifications, SchoolNotifications } from '../school-notifications.model';

import { SchoolNotificationsService } from './school-notifications.service';

describe('Service Tests', () => {
  describe('SchoolNotifications Service', () => {
    let service: SchoolNotificationsService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolNotifications;
    let expectedResult: ISchoolNotifications | ISchoolNotifications[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolNotificationsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        notificationTitle: 'AAAAAAA',
        notificationDetails: 'AAAAAAA',
        notificationFileContentType: 'image/png',
        notificationFile: 'AAAAAAA',
        notificationFileLink: 'AAAAAAA',
        showTillDate: currentDate,
        createDate: currentDate,
        lastModified: currentDate,
        cancelDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            showTillDate: currentDate.format(DATE_FORMAT),
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

      it('should create a SchoolNotifications', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            showTillDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            showTillDate: currentDate,
            createDate: currentDate,
            lastModified: currentDate,
            cancelDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SchoolNotifications()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolNotifications', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            notificationTitle: 'BBBBBB',
            notificationDetails: 'BBBBBB',
            notificationFile: 'BBBBBB',
            notificationFileLink: 'BBBBBB',
            showTillDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            showTillDate: currentDate,
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

      it('should partial update a SchoolNotifications', () => {
        const patchObject = Object.assign(
          {
            notificationDetails: 'BBBBBB',
            notificationFileLink: 'BBBBBB',
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
          },
          new SchoolNotifications()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            showTillDate: currentDate,
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

      it('should return a list of SchoolNotifications', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            notificationTitle: 'BBBBBB',
            notificationDetails: 'BBBBBB',
            notificationFile: 'BBBBBB',
            notificationFileLink: 'BBBBBB',
            showTillDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_FORMAT),
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            showTillDate: currentDate,
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

      it('should delete a SchoolNotifications', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolNotificationsToCollectionIfMissing', () => {
        it('should add a SchoolNotifications to an empty array', () => {
          const schoolNotifications: ISchoolNotifications = { id: 123 };
          expectedResult = service.addSchoolNotificationsToCollectionIfMissing([], schoolNotifications);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolNotifications);
        });

        it('should not add a SchoolNotifications to an array that contains it', () => {
          const schoolNotifications: ISchoolNotifications = { id: 123 };
          const schoolNotificationsCollection: ISchoolNotifications[] = [
            {
              ...schoolNotifications,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolNotificationsToCollectionIfMissing(schoolNotificationsCollection, schoolNotifications);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolNotifications to an array that doesn't contain it", () => {
          const schoolNotifications: ISchoolNotifications = { id: 123 };
          const schoolNotificationsCollection: ISchoolNotifications[] = [{ id: 456 }];
          expectedResult = service.addSchoolNotificationsToCollectionIfMissing(schoolNotificationsCollection, schoolNotifications);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolNotifications);
        });

        it('should add only unique SchoolNotifications to an array', () => {
          const schoolNotificationsArray: ISchoolNotifications[] = [{ id: 123 }, { id: 456 }, { id: 44526 }];
          const schoolNotificationsCollection: ISchoolNotifications[] = [{ id: 123 }];
          expectedResult = service.addSchoolNotificationsToCollectionIfMissing(schoolNotificationsCollection, ...schoolNotificationsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolNotifications: ISchoolNotifications = { id: 123 };
          const schoolNotifications2: ISchoolNotifications = { id: 456 };
          expectedResult = service.addSchoolNotificationsToCollectionIfMissing([], schoolNotifications, schoolNotifications2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolNotifications);
          expect(expectedResult).toContain(schoolNotifications2);
        });

        it('should accept null and undefined values', () => {
          const schoolNotifications: ISchoolNotifications = { id: 123 };
          expectedResult = service.addSchoolNotificationsToCollectionIfMissing([], null, schoolNotifications, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolNotifications);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

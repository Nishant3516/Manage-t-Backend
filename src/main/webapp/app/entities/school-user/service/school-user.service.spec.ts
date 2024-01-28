import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { UserType } from 'app/entities/enumerations/user-type.model';
import { ISchoolUser, SchoolUser } from '../school-user.model';

import { SchoolUserService } from './school-user.service';

describe('Service Tests', () => {
  describe('SchoolUser Service', () => {
    let service: SchoolUserService;
    let httpMock: HttpTestingController;
    let elemDefault: ISchoolUser;
    let expectedResult: ISchoolUser | ISchoolUser[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SchoolUserService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        loginName: 'AAAAAAA',
        password: 'AAAAAAA',
        userEmail: 'AAAAAAA',
        extraInfo: 'AAAAAAA',
        activated: false,
        userType: UserType.STUDENT,
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

      it('should create a SchoolUser', () => {
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

        service.create(new SchoolUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SchoolUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            loginName: 'BBBBBB',
            password: 'BBBBBB',
            userEmail: 'BBBBBB',
            extraInfo: 'BBBBBB',
            activated: true,
            userType: 'BBBBBB',
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

      it('should partial update a SchoolUser', () => {
        const patchObject = Object.assign(
          {
            loginName: 'BBBBBB',
            userEmail: 'BBBBBB',
            activated: true,
            userType: 'BBBBBB',
          },
          new SchoolUser()
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

      it('should return a list of SchoolUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            loginName: 'BBBBBB',
            password: 'BBBBBB',
            userEmail: 'BBBBBB',
            extraInfo: 'BBBBBB',
            activated: true,
            userType: 'BBBBBB',
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

      it('should delete a SchoolUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSchoolUserToCollectionIfMissing', () => {
        it('should add a SchoolUser to an empty array', () => {
          const schoolUser: ISchoolUser = { id: 123 };
          expectedResult = service.addSchoolUserToCollectionIfMissing([], schoolUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolUser);
        });

        it('should not add a SchoolUser to an array that contains it', () => {
          const schoolUser: ISchoolUser = { id: 123 };
          const schoolUserCollection: ISchoolUser[] = [
            {
              ...schoolUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addSchoolUserToCollectionIfMissing(schoolUserCollection, schoolUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SchoolUser to an array that doesn't contain it", () => {
          const schoolUser: ISchoolUser = { id: 123 };
          const schoolUserCollection: ISchoolUser[] = [{ id: 456 }];
          expectedResult = service.addSchoolUserToCollectionIfMissing(schoolUserCollection, schoolUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolUser);
        });

        it('should add only unique SchoolUser to an array', () => {
          const schoolUserArray: ISchoolUser[] = [{ id: 123 }, { id: 456 }, { id: 67084 }];
          const schoolUserCollection: ISchoolUser[] = [{ id: 123 }];
          expectedResult = service.addSchoolUserToCollectionIfMissing(schoolUserCollection, ...schoolUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const schoolUser: ISchoolUser = { id: 123 };
          const schoolUser2: ISchoolUser = { id: 456 };
          expectedResult = service.addSchoolUserToCollectionIfMissing([], schoolUser, schoolUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(schoolUser);
          expect(expectedResult).toContain(schoolUser2);
        });

        it('should accept null and undefined values', () => {
          const schoolUser: ISchoolUser = { id: 123 };
          expectedResult = service.addSchoolUserToCollectionIfMissing([], null, schoolUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(schoolUser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

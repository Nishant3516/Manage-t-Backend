import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IClassSubject, ClassSubject } from '../class-subject.model';

import { ClassSubjectService } from './class-subject.service';

describe('Service Tests', () => {
  describe('ClassSubject Service', () => {
    let service: ClassSubjectService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassSubject;
    let expectedResult: IClassSubject | IClassSubject[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassSubjectService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        subjectName: 'AAAAAAA',
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

      it('should create a ClassSubject', () => {
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

        service.create(new ClassSubject()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassSubject', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            subjectName: 'BBBBBB',
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

      it('should partial update a ClassSubject', () => {
        const patchObject = Object.assign(
          {
            lastModified: currentDate.format(DATE_FORMAT),
            cancelDate: currentDate.format(DATE_FORMAT),
          },
          new ClassSubject()
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

      it('should return a list of ClassSubject', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            subjectName: 'BBBBBB',
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

      it('should delete a ClassSubject', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassSubjectToCollectionIfMissing', () => {
        it('should add a ClassSubject to an empty array', () => {
          const classSubject: IClassSubject = { id: 123 };
          expectedResult = service.addClassSubjectToCollectionIfMissing([], classSubject);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classSubject);
        });

        it('should not add a ClassSubject to an array that contains it', () => {
          const classSubject: IClassSubject = { id: 123 };
          const classSubjectCollection: IClassSubject[] = [
            {
              ...classSubject,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassSubjectToCollectionIfMissing(classSubjectCollection, classSubject);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassSubject to an array that doesn't contain it", () => {
          const classSubject: IClassSubject = { id: 123 };
          const classSubjectCollection: IClassSubject[] = [{ id: 456 }];
          expectedResult = service.addClassSubjectToCollectionIfMissing(classSubjectCollection, classSubject);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classSubject);
        });

        it('should add only unique ClassSubject to an array', () => {
          const classSubjectArray: IClassSubject[] = [{ id: 123 }, { id: 456 }, { id: 65902 }];
          const classSubjectCollection: IClassSubject[] = [{ id: 123 }];
          expectedResult = service.addClassSubjectToCollectionIfMissing(classSubjectCollection, ...classSubjectArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classSubject: IClassSubject = { id: 123 };
          const classSubject2: IClassSubject = { id: 456 };
          expectedResult = service.addClassSubjectToCollectionIfMissing([], classSubject, classSubject2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classSubject);
          expect(expectedResult).toContain(classSubject2);
        });

        it('should accept null and undefined values', () => {
          const classSubject: IClassSubject = { id: 123 };
          expectedResult = service.addClassSubjectToCollectionIfMissing([], null, classSubject, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classSubject);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

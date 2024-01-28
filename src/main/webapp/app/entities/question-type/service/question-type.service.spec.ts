import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IQuestionType, QuestionType } from '../question-type.model';

import { QuestionTypeService } from './question-type.service';

describe('QuestionType Service', () => {
  let service: QuestionTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuestionType;
  let expectedResult: IQuestionType | IQuestionType[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionTypeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      questionType: 'AAAAAAA',
      marks: 0,
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

    it('should create a QuestionType', () => {
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

      service.create(new QuestionType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuestionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          questionType: 'BBBBBB',
          marks: 1,
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

    it('should partial update a QuestionType', () => {
      const patchObject = Object.assign(
        {
          marks: 1,
          createDate: currentDate.format(DATE_FORMAT),
          lastModified: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        new QuestionType()
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

    it('should return a list of QuestionType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          questionType: 'BBBBBB',
          marks: 1,
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

    it('should delete a QuestionType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuestionTypeToCollectionIfMissing', () => {
      it('should add a QuestionType to an empty array', () => {
        const questionType: IQuestionType = { id: 123 };
        expectedResult = service.addQuestionTypeToCollectionIfMissing([], questionType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionType);
      });

      it('should not add a QuestionType to an array that contains it', () => {
        const questionType: IQuestionType = { id: 123 };
        const questionTypeCollection: IQuestionType[] = [
          {
            ...questionType,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuestionTypeToCollectionIfMissing(questionTypeCollection, questionType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuestionType to an array that doesn't contain it", () => {
        const questionType: IQuestionType = { id: 123 };
        const questionTypeCollection: IQuestionType[] = [{ id: 456 }];
        expectedResult = service.addQuestionTypeToCollectionIfMissing(questionTypeCollection, questionType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionType);
      });

      it('should add only unique QuestionType to an array', () => {
        const questionTypeArray: IQuestionType[] = [{ id: 123 }, { id: 456 }, { id: 7664 }];
        const questionTypeCollection: IQuestionType[] = [{ id: 123 }];
        expectedResult = service.addQuestionTypeToCollectionIfMissing(questionTypeCollection, ...questionTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionType: IQuestionType = { id: 123 };
        const questionType2: IQuestionType = { id: 456 };
        expectedResult = service.addQuestionTypeToCollectionIfMissing([], questionType, questionType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionType);
        expect(expectedResult).toContain(questionType2);
      });

      it('should accept null and undefined values', () => {
        const questionType: IQuestionType = { id: 123 };
        expectedResult = service.addQuestionTypeToCollectionIfMissing([], null, questionType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionType);
      });

      it('should return initial array if no QuestionType is added', () => {
        const questionTypeCollection: IQuestionType[] = [{ id: 123 }];
        expectedResult = service.addQuestionTypeToCollectionIfMissing(questionTypeCollection, undefined, null);
        expect(expectedResult).toEqual(questionTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

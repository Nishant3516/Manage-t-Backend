import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Status } from 'app/entities/enumerations/status.model';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';
import { IQuestion, Question } from '../question.model';

import { QuestionService } from './question.service';

describe('Question Service', () => {
  let service: QuestionService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuestion;
  let expectedResult: IQuestion | IQuestion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      questionImportFileContentType: 'image/png',
      questionImportFile: 'AAAAAAA',
      questionText: 'AAAAAAA',
      questionImageContentType: 'image/png',
      questionImage: 'AAAAAAA',
      answerImageContentType: 'image/png',
      answerImage: 'AAAAAAA',
      imageSideBySide: false,
      option1: 'AAAAAAA',
      option2: 'AAAAAAA',
      option3: 'AAAAAAA',
      option4: 'AAAAAAA',
      option5: 'AAAAAAA',
      status: Status.DRAFT,
      weightage: 0,
      difficultyLevel: Difficulty.EASY,
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

    it('should create a Question', () => {
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

      service.create(new Question()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Question', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          questionImportFile: 'BBBBBB',
          questionText: 'BBBBBB',
          questionImage: 'BBBBBB',
          answerImage: 'BBBBBB',
          imageSideBySide: true,
          option1: 'BBBBBB',
          option2: 'BBBBBB',
          option3: 'BBBBBB',
          option4: 'BBBBBB',
          option5: 'BBBBBB',
          status: 'BBBBBB',
          weightage: 1,
          difficultyLevel: 'BBBBBB',
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

    it('should partial update a Question', () => {
      const patchObject = Object.assign(
        {
          questionImportFile: 'BBBBBB',
          questionImage: 'BBBBBB',
          option1: 'BBBBBB',
          option2: 'BBBBBB',
          option3: 'BBBBBB',
          option5: 'BBBBBB',
          status: 'BBBBBB',
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        new Question()
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

    it('should return a list of Question', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          questionImportFile: 'BBBBBB',
          questionText: 'BBBBBB',
          questionImage: 'BBBBBB',
          answerImage: 'BBBBBB',
          imageSideBySide: true,
          option1: 'BBBBBB',
          option2: 'BBBBBB',
          option3: 'BBBBBB',
          option4: 'BBBBBB',
          option5: 'BBBBBB',
          status: 'BBBBBB',
          weightage: 1,
          difficultyLevel: 'BBBBBB',
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

    it('should delete a Question', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuestionToCollectionIfMissing', () => {
      it('should add a Question to an empty array', () => {
        const question: IQuestion = { id: 123 };
        expectedResult = service.addQuestionToCollectionIfMissing([], question);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(question);
      });

      it('should not add a Question to an array that contains it', () => {
        const question: IQuestion = { id: 123 };
        const questionCollection: IQuestion[] = [
          {
            ...question,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuestionToCollectionIfMissing(questionCollection, question);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Question to an array that doesn't contain it", () => {
        const question: IQuestion = { id: 123 };
        const questionCollection: IQuestion[] = [{ id: 456 }];
        expectedResult = service.addQuestionToCollectionIfMissing(questionCollection, question);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(question);
      });

      it('should add only unique Question to an array', () => {
        const questionArray: IQuestion[] = [{ id: 123 }, { id: 456 }, { id: 10446 }];
        const questionCollection: IQuestion[] = [{ id: 123 }];
        expectedResult = service.addQuestionToCollectionIfMissing(questionCollection, ...questionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const question: IQuestion = { id: 123 };
        const question2: IQuestion = { id: 456 };
        expectedResult = service.addQuestionToCollectionIfMissing([], question, question2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(question);
        expect(expectedResult).toContain(question2);
      });

      it('should accept null and undefined values', () => {
        const question: IQuestion = { id: 123 };
        expectedResult = service.addQuestionToCollectionIfMissing([], null, question, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(question);
      });

      it('should return initial array if no Question is added', () => {
        const questionCollection: IQuestion[] = [{ id: 123 }];
        expectedResult = service.addQuestionToCollectionIfMissing(questionCollection, undefined, null);
        expect(expectedResult).toEqual(questionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

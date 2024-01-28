import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IQuestionPaper, QuestionPaper } from '../question-paper.model';

import { QuestionPaperService } from './question-paper.service';

describe('QuestionPaper Service', () => {
  let service: QuestionPaperService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuestionPaper;
  let expectedResult: IQuestionPaper | IQuestionPaper[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionPaperService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      tenatLogoContentType: 'image/png',
      tenatLogo: 'AAAAAAA',
      questionPaperFileContentType: 'image/png',
      questionPaperFile: 'AAAAAAA',
      questionPaperName: 'AAAAAAA',
      mainTitle: 'AAAAAAA',
      subTitle: 'AAAAAAA',
      leftSubHeading1: 'AAAAAAA',
      leftSubHeading2: 'AAAAAAA',
      rightSubHeading1: 'AAAAAAA',
      rightSubHeading2: 'AAAAAAA',
      instructions: 'AAAAAAA',
      footerText: 'AAAAAAA',
      totalMarks: 0,
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

    it('should create a QuestionPaper', () => {
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

      service.create(new QuestionPaper()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuestionPaper', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tenatLogo: 'BBBBBB',
          questionPaperFile: 'BBBBBB',
          questionPaperName: 'BBBBBB',
          mainTitle: 'BBBBBB',
          subTitle: 'BBBBBB',
          leftSubHeading1: 'BBBBBB',
          leftSubHeading2: 'BBBBBB',
          rightSubHeading1: 'BBBBBB',
          rightSubHeading2: 'BBBBBB',
          instructions: 'BBBBBB',
          footerText: 'BBBBBB',
          totalMarks: 1,
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

    it('should partial update a QuestionPaper', () => {
      const patchObject = Object.assign(
        {
          questionPaperName: 'BBBBBB',
          mainTitle: 'BBBBBB',
          subTitle: 'BBBBBB',
          leftSubHeading1: 'BBBBBB',
          leftSubHeading2: 'BBBBBB',
          rightSubHeading2: 'BBBBBB',
          instructions: 'BBBBBB',
          lastModified: currentDate.format(DATE_FORMAT),
          cancelDate: currentDate.format(DATE_FORMAT),
        },
        new QuestionPaper()
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

    it('should return a list of QuestionPaper', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tenatLogo: 'BBBBBB',
          questionPaperFile: 'BBBBBB',
          questionPaperName: 'BBBBBB',
          mainTitle: 'BBBBBB',
          subTitle: 'BBBBBB',
          leftSubHeading1: 'BBBBBB',
          leftSubHeading2: 'BBBBBB',
          rightSubHeading1: 'BBBBBB',
          rightSubHeading2: 'BBBBBB',
          instructions: 'BBBBBB',
          footerText: 'BBBBBB',
          totalMarks: 1,
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

    it('should delete a QuestionPaper', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuestionPaperToCollectionIfMissing', () => {
      it('should add a QuestionPaper to an empty array', () => {
        const questionPaper: IQuestionPaper = { id: 123 };
        expectedResult = service.addQuestionPaperToCollectionIfMissing([], questionPaper);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionPaper);
      });

      it('should not add a QuestionPaper to an array that contains it', () => {
        const questionPaper: IQuestionPaper = { id: 123 };
        const questionPaperCollection: IQuestionPaper[] = [
          {
            ...questionPaper,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuestionPaperToCollectionIfMissing(questionPaperCollection, questionPaper);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuestionPaper to an array that doesn't contain it", () => {
        const questionPaper: IQuestionPaper = { id: 123 };
        const questionPaperCollection: IQuestionPaper[] = [{ id: 456 }];
        expectedResult = service.addQuestionPaperToCollectionIfMissing(questionPaperCollection, questionPaper);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionPaper);
      });

      it('should add only unique QuestionPaper to an array', () => {
        const questionPaperArray: IQuestionPaper[] = [{ id: 123 }, { id: 456 }, { id: 59917 }];
        const questionPaperCollection: IQuestionPaper[] = [{ id: 123 }];
        expectedResult = service.addQuestionPaperToCollectionIfMissing(questionPaperCollection, ...questionPaperArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionPaper: IQuestionPaper = { id: 123 };
        const questionPaper2: IQuestionPaper = { id: 456 };
        expectedResult = service.addQuestionPaperToCollectionIfMissing([], questionPaper, questionPaper2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionPaper);
        expect(expectedResult).toContain(questionPaper2);
      });

      it('should accept null and undefined values', () => {
        const questionPaper: IQuestionPaper = { id: 123 };
        expectedResult = service.addQuestionPaperToCollectionIfMissing([], null, questionPaper, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionPaper);
      });

      it('should return initial array if no QuestionPaper is added', () => {
        const questionPaperCollection: IQuestionPaper[] = [{ id: 123 }];
        expectedResult = service.addQuestionPaperToCollectionIfMissing(questionPaperCollection, undefined, null);
        expect(expectedResult).toEqual(questionPaperCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

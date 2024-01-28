import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionPaper, getQuestionPaperIdentifier } from '../question-paper.model';

export type EntityResponseType = HttpResponse<IQuestionPaper>;
export type EntityArrayResponseType = HttpResponse<IQuestionPaper[]>;

@Injectable({ providedIn: 'root' })
export class QuestionPaperService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/question-papers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionPaper: IQuestionPaper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questionPaper);
    return this.http
      .post<IQuestionPaper>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(questionPaper: IQuestionPaper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questionPaper);
    return this.http
      .put<IQuestionPaper>(`${this.resourceUrl}/${getQuestionPaperIdentifier(questionPaper) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(questionPaper: IQuestionPaper): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questionPaper);
    return this.http
      .patch<IQuestionPaper>(`${this.resourceUrl}/${getQuestionPaperIdentifier(questionPaper) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IQuestionPaper>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IQuestionPaper[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionPaperToCollectionIfMissing(
    questionPaperCollection: IQuestionPaper[],
    ...questionPapersToCheck: (IQuestionPaper | null | undefined)[]
  ): IQuestionPaper[] {
    const questionPapers: IQuestionPaper[] = questionPapersToCheck.filter(isPresent);
    if (questionPapers.length > 0) {
      const questionPaperCollectionIdentifiers = questionPaperCollection.map(
        questionPaperItem => getQuestionPaperIdentifier(questionPaperItem)!
      );
      const questionPapersToAdd = questionPapers.filter(questionPaperItem => {
        const questionPaperIdentifier = getQuestionPaperIdentifier(questionPaperItem);
        if (questionPaperIdentifier == null || questionPaperCollectionIdentifiers.includes(questionPaperIdentifier)) {
          return false;
        }
        questionPaperCollectionIdentifiers.push(questionPaperIdentifier);
        return true;
      });
      return [...questionPapersToAdd, ...questionPaperCollection];
    }
    return questionPaperCollection;
  }

  protected convertDateFromClient(questionPaper: IQuestionPaper): IQuestionPaper {
    return Object.assign({}, questionPaper, {
      createDate: questionPaper.createDate?.isValid() ? questionPaper.createDate.format(DATE_FORMAT) : undefined,
      lastModified: questionPaper.lastModified?.isValid() ? questionPaper.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: questionPaper.cancelDate?.isValid() ? questionPaper.cancelDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
      res.body.cancelDate = res.body.cancelDate ? dayjs(res.body.cancelDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((questionPaper: IQuestionPaper) => {
        questionPaper.createDate = questionPaper.createDate ? dayjs(questionPaper.createDate) : undefined;
        questionPaper.lastModified = questionPaper.lastModified ? dayjs(questionPaper.lastModified) : undefined;
        questionPaper.cancelDate = questionPaper.cancelDate ? dayjs(questionPaper.cancelDate) : undefined;
      });
    }
    return res;
  }
}

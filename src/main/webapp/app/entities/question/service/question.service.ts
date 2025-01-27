import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestion, getQuestionIdentifier } from '../question.model';

export type EntityResponseType = HttpResponse<IQuestion>;
export type EntityArrayResponseType = HttpResponse<IQuestion[]>;

@Injectable({ providedIn: 'root' })
export class QuestionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(question: IQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(question);
    return this.http
      .post<IQuestion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(question: IQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(question);
    return this.http
      .put<IQuestion>(`${this.resourceUrl}/${getQuestionIdentifier(question) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(question: IQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(question);
    return this.http
      .patch<IQuestion>(`${this.resourceUrl}/${getQuestionIdentifier(question) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IQuestion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IQuestion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionToCollectionIfMissing(questionCollection: IQuestion[], ...questionsToCheck: (IQuestion | null | undefined)[]): IQuestion[] {
    const questions: IQuestion[] = questionsToCheck.filter(isPresent);
    if (questions.length > 0) {
      const questionCollectionIdentifiers = questionCollection.map(questionItem => getQuestionIdentifier(questionItem)!);
      const questionsToAdd = questions.filter(questionItem => {
        const questionIdentifier = getQuestionIdentifier(questionItem);
        if (questionIdentifier == null || questionCollectionIdentifiers.includes(questionIdentifier)) {
          return false;
        }
        questionCollectionIdentifiers.push(questionIdentifier);
        return true;
      });
      return [...questionsToAdd, ...questionCollection];
    }
    return questionCollection;
  }

  protected convertDateFromClient(question: IQuestion): IQuestion {
    return Object.assign({}, question, {
      createDate: question.createDate?.isValid() ? question.createDate.format(DATE_FORMAT) : undefined,
      lastModified: question.lastModified?.isValid() ? question.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: question.cancelDate?.isValid() ? question.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((question: IQuestion) => {
        question.createDate = question.createDate ? dayjs(question.createDate) : undefined;
        question.lastModified = question.lastModified ? dayjs(question.lastModified) : undefined;
        question.cancelDate = question.cancelDate ? dayjs(question.cancelDate) : undefined;
      });
    }
    return res;
  }
}

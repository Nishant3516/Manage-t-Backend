import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionType, getQuestionTypeIdentifier } from '../question-type.model';

export type EntityResponseType = HttpResponse<IQuestionType>;
export type EntityArrayResponseType = HttpResponse<IQuestionType[]>;

@Injectable({ providedIn: 'root' })
export class QuestionTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/question-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionType: IQuestionType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questionType);
    return this.http
      .post<IQuestionType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(questionType: IQuestionType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questionType);
    return this.http
      .put<IQuestionType>(`${this.resourceUrl}/${getQuestionTypeIdentifier(questionType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(questionType: IQuestionType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questionType);
    return this.http
      .patch<IQuestionType>(`${this.resourceUrl}/${getQuestionTypeIdentifier(questionType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IQuestionType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IQuestionType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionTypeToCollectionIfMissing(
    questionTypeCollection: IQuestionType[],
    ...questionTypesToCheck: (IQuestionType | null | undefined)[]
  ): IQuestionType[] {
    const questionTypes: IQuestionType[] = questionTypesToCheck.filter(isPresent);
    if (questionTypes.length > 0) {
      const questionTypeCollectionIdentifiers = questionTypeCollection.map(
        questionTypeItem => getQuestionTypeIdentifier(questionTypeItem)!
      );
      const questionTypesToAdd = questionTypes.filter(questionTypeItem => {
        const questionTypeIdentifier = getQuestionTypeIdentifier(questionTypeItem);
        if (questionTypeIdentifier == null || questionTypeCollectionIdentifiers.includes(questionTypeIdentifier)) {
          return false;
        }
        questionTypeCollectionIdentifiers.push(questionTypeIdentifier);
        return true;
      });
      return [...questionTypesToAdd, ...questionTypeCollection];
    }
    return questionTypeCollection;
  }

  protected convertDateFromClient(questionType: IQuestionType): IQuestionType {
    return Object.assign({}, questionType, {
      createDate: questionType.createDate?.isValid() ? questionType.createDate.format(DATE_FORMAT) : undefined,
      lastModified: questionType.lastModified?.isValid() ? questionType.lastModified.format(DATE_FORMAT) : undefined,
      cancelDate: questionType.cancelDate?.isValid() ? questionType.cancelDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((questionType: IQuestionType) => {
        questionType.createDate = questionType.createDate ? dayjs(questionType.createDate) : undefined;
        questionType.lastModified = questionType.lastModified ? dayjs(questionType.lastModified) : undefined;
        questionType.cancelDate = questionType.cancelDate ? dayjs(questionType.cancelDate) : undefined;
      });
    }
    return res;
  }
}

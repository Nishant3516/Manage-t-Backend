import * as dayjs from 'dayjs';
import { IQuestion } from 'app/entities/question/question.model';
import { ITenant } from 'app/entities/tenant/tenant.model';

export interface IQuestionType {
  id?: number;
  questionType?: string | null;
  marks?: number | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  questions?: IQuestion[] | null;
  tenant?: ITenant | null;
}

export class QuestionType implements IQuestionType {
  constructor(
    public id?: number,
    public questionType?: string | null,
    public marks?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public questions?: IQuestion[] | null,
    public tenant?: ITenant | null
  ) {}
}

export function getQuestionTypeIdentifier(questionType: IQuestionType): number | undefined {
  return questionType.id;
}

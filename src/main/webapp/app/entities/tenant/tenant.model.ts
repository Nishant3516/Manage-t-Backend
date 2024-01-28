import * as dayjs from 'dayjs';
import { IQuestion } from 'app/entities/question/question.model';
import { IQuestionType } from 'app/entities/question-type/question-type.model';
import { IQuestionPaper } from 'app/entities/question-paper/question-paper.model';
import { ITag } from 'app/entities/tag/tag.model';

export interface ITenant {
  id?: number;
  tenantName?: string | null;
  tenantLogoContentType?: string | null;
  tenantLogo?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  questions?: IQuestion[] | null;
  questionTypes?: IQuestionType[] | null;
  questionPapers?: IQuestionPaper[] | null;
  tags?: ITag[] | null;
}

export class Tenant implements ITenant {
  constructor(
    public id?: number,
    public tenantName?: string | null,
    public tenantLogoContentType?: string | null,
    public tenantLogo?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public questions?: IQuestion[] | null,
    public questionTypes?: IQuestionType[] | null,
    public questionPapers?: IQuestionPaper[] | null,
    public tags?: ITag[] | null
  ) {}
}

export function getTenantIdentifier(tenant: ITenant): number | undefined {
  return tenant.id;
}

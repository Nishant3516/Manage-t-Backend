import * as dayjs from 'dayjs';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { IQuestionPaper } from 'app/entities/question-paper/question-paper.model';
import { IQuestion } from 'app/entities/question/question.model';
import { TagLevel } from 'app/entities/enumerations/tag-level.model';

export interface ITag {
  id?: number;
  tagText?: string | null;
  tagLevel?: TagLevel | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  tenant?: ITenant | null;
  questionPapers?: IQuestionPaper[] | null;
  questions?: IQuestion[] | null;
}

export class Tag implements ITag {
  constructor(
    public id?: number,
    public tagText?: string | null,
    public tagLevel?: TagLevel | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public tenant?: ITenant | null,
    public questionPapers?: IQuestionPaper[] | null,
    public questions?: IQuestion[] | null
  ) {}
}

export function getTagIdentifier(tag: ITag): number | undefined {
  return tag.id;
}

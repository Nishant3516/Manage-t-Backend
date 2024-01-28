import * as dayjs from 'dayjs';
import { IQuestion } from 'app/entities/question/question.model';
import { ITag } from 'app/entities/tag/tag.model';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';

export interface IQuestionPaper {
  id?: number;
  tenatLogoContentType?: string | null;
  tenatLogo?: string | null;
  questionPaperFileContentType?: string | null;
  questionPaperFile?: string | null;
  questionPaperName?: string | null;
  mainTitle?: string | null;
  subTitle?: string | null;
  leftSubHeading1?: string | null;
  leftSubHeading2?: string | null;
  rightSubHeading1?: string | null;
  rightSubHeading2?: string | null;
  instructions?: string | null;
  footerText?: string | null;
  totalMarks?: number | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  questions?: IQuestion[] | null;
  tags?: ITag[] | null;
  tenant?: ITenant | null;
  schoolClass?: ISchoolClass | null;
  classSubject?: IClassSubject | null;
}

export class QuestionPaper implements IQuestionPaper {
  constructor(
    public id?: number,
    public tenatLogoContentType?: string | null,
    public tenatLogo?: string | null,
    public questionPaperFileContentType?: string | null,
    public questionPaperFile?: string | null,
    public questionPaperName?: string | null,
    public mainTitle?: string | null,
    public subTitle?: string | null,
    public leftSubHeading1?: string | null,
    public leftSubHeading2?: string | null,
    public rightSubHeading1?: string | null,
    public rightSubHeading2?: string | null,
    public instructions?: string | null,
    public footerText?: string | null,
    public totalMarks?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public questions?: IQuestion[] | null,
    public tags?: ITag[] | null,
    public tenant?: ITenant | null,
    public schoolClass?: ISchoolClass | null,
    public classSubject?: IClassSubject | null
  ) {}
}

export function getQuestionPaperIdentifier(questionPaper: IQuestionPaper): number | undefined {
  return questionPaper.id;
}

import * as dayjs from 'dayjs';
import { ITag } from 'app/entities/tag/tag.model';
import { IQuestionType } from 'app/entities/question-type/question-type.model';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ISubjectChapter } from 'app/entities/subject-chapter/subject-chapter.model';
import { IQuestionPaper } from 'app/entities/question-paper/question-paper.model';
import { Status } from 'app/entities/enumerations/status.model';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';

export interface IQuestion {
  id?: number;
  questionImportFileContentType?: string | null;
  questionImportFile?: string | null;
  questionText?: string | null;
  questionImageContentType?: string | null;
  questionImage?: string | null;
  answerImageContentType?: string | null;
  answerImage?: string | null;
  imageSideBySide?: boolean | null;
  option1?: string | null;
  option2?: string | null;
  option3?: string | null;
  option4?: string | null;
  option5?: string | null;
  status?: Status | null;
  weightage?: number | null;
  difficultyLevel?: Difficulty | null;
  createDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  tags?: ITag[] | null;
  questionType?: IQuestionType | null;
  tenant?: ITenant | null;
  schoolClass?: ISchoolClass | null;
  classSubject?: IClassSubject | null;
  subjectChapter?: ISubjectChapter | null;
  questionPapers?: IQuestionPaper[] | null;
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public questionImportFileContentType?: string | null,
    public questionImportFile?: string | null,
    public questionText?: string | null,
    public questionImageContentType?: string | null,
    public questionImage?: string | null,
    public answerImageContentType?: string | null,
    public answerImage?: string | null,
    public imageSideBySide?: boolean | null,
    public option1?: string | null,
    public option2?: string | null,
    public option3?: string | null,
    public option4?: string | null,
    public option5?: string | null,
    public status?: Status | null,
    public weightage?: number | null,
    public difficultyLevel?: Difficulty | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public cancelDate?: dayjs.Dayjs | null,
    public tags?: ITag[] | null,
    public questionType?: IQuestionType | null,
    public tenant?: ITenant | null,
    public schoolClass?: ISchoolClass | null,
    public classSubject?: IClassSubject | null,
    public subjectChapter?: ISubjectChapter | null,
    public questionPapers?: IQuestionPaper[] | null
  ) {
    this.imageSideBySide = this.imageSideBySide ?? false;
  }
}

export function getQuestionIdentifier(question: IQuestion): number | undefined {
  return question.id;
}

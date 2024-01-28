import { NgModule } from '@angular/core';
import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { ClassDropdownComponent } from './filters/class.dropdown.component';
import { ClassSubjectDropdownComponent } from './filters/class.subject.dropdown.component';
import { ClassStudentDropdownComponent } from './filters/class.student.dropdown.component';
import { SubjectDropdownComponent } from './filters/subject.dropdown.component';
import { ChapterDropdownComponent } from './filters/chapter.dropdown.component';

import { StudentDropdownComponent } from './filters/student.dropdown.component';
import { QuestionTypeDropdownComponent } from './filters/questiontype.dropdown.component';

@NgModule({
  imports: [SharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    ClassDropdownComponent,
    ClassSubjectDropdownComponent,
    ClassStudentDropdownComponent,
    SubjectDropdownComponent,
    ChapterDropdownComponent,
    StudentDropdownComponent,
	QuestionTypeDropdownComponent
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    ClassDropdownComponent,
    ClassSubjectDropdownComponent,
    ClassStudentDropdownComponent,
    SubjectDropdownComponent,
    ChapterDropdownComponent,
    StudentDropdownComponent,
	QuestionTypeDropdownComponent
  ],
})
export class SharedModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'audit-log',
        data: { pageTitle: 'manageitApp.auditLog.home.title' },
        loadChildren: () => import('./audit-log/audit-log.module').then(m => m.AuditLogModule),
      },
      {
        path: 'school-report',
        data: { pageTitle: 'manageitApp.schoolReport.home.title' },
        loadChildren: () => import('./school-report/school-report.module').then(m => m.SchoolReportModule),
      },
      {
        path: 'school-days-off',
        data: { pageTitle: 'manageitApp.schoolDaysOff.home.title' },
        loadChildren: () => import('./school-days-off/school-days-off.module').then(m => m.SchoolDaysOffModule),
      },
      {
        path: 'school-event',
        data: { pageTitle: 'manageitApp.schoolEvent.home.title' },
        loadChildren: () => import('./school-event/school-event.module').then(m => m.SchoolEventModule),
      },
      {
        path: 'school-picture-gallery',
        data: { pageTitle: 'manageitApp.schoolPictureGallery.home.title' },
        loadChildren: () => import('./school-picture-gallery/school-picture-gallery.module').then(m => m.SchoolPictureGalleryModule),
      },
      {
        path: 'school-video-gallery',
        data: { pageTitle: 'manageitApp.schoolVideoGallery.home.title' },
        loadChildren: () => import('./school-video-gallery/school-video-gallery.module').then(m => m.SchoolVideoGalleryModule),
      },
      {
        path: 'school-user',
        data: { pageTitle: 'manageitApp.schoolUser.home.title' },
        loadChildren: () => import('./school-user/school-user.module').then(m => m.SchoolUserModule),
      },
      {
        path: 'school',
        data: { pageTitle: 'manageitApp.school.home.title' },
        loadChildren: () => import('./school/school.module').then(m => m.SchoolModule),
      },
      {
        path: 'school-class',
        data: { pageTitle: 'manageitApp.schoolClass.home.title' },
        loadChildren: () => import('./school-class/school-class.module').then(m => m.SchoolClassModule),
      },
      {
        path: 'id-store',
        data: { pageTitle: 'manageitApp.idStore.home.title' },
        loadChildren: () => import('./id-store/id-store.module').then(m => m.IdStoreModule),
      },
      {
        path: 'school-notifications',
        data: { pageTitle: 'manageitApp.schoolNotifications.home.title' },
        loadChildren: () => import('./school-notifications/school-notifications.module').then(m => m.SchoolNotificationsModule),
      },
      {
        path: 'class-student',
        data: { pageTitle: 'manageitApp.classStudent.home.title' },
        loadChildren: () => import('./class-student/class-student.module').then(m => m.ClassStudentModule),
      },
      {
        path: 'school-ledger-head',
        data: { pageTitle: 'manageitApp.schoolLedgerHead.home.title' },
        loadChildren: () => import('./school-ledger-head/school-ledger-head.module').then(m => m.SchoolLedgerHeadModule),
      },
      {
        path: 'class-fee',
        data: { pageTitle: 'manageitApp.classFee.home.title' },
        loadChildren: () => import('./class-fee/class-fee.module').then(m => m.ClassFeeModule),
      },
      {
        path: 'student-discount',
        data: { pageTitle: 'manageitApp.studentDiscount.home.title' },
        loadChildren: () => import('./student-discount/student-discount.module').then(m => m.StudentDiscountModule),
      },
      {
        path: 'student-additional-charges',
        data: { pageTitle: 'manageitApp.studentAdditionalCharges.home.title' },
        loadChildren: () =>
          import('./student-additional-charges/student-additional-charges.module').then(m => m.StudentAdditionalChargesModule),
      },
      {
        path: 'student-payments',
        data: { pageTitle: 'manageitApp.studentPayments.home.title' },
        loadChildren: () => import('./student-payments/student-payments.module').then(m => m.StudentPaymentsModule),
      },
      {
        path: 'student-attendence',
        data: { pageTitle: 'manageitApp.studentAttendence.home.title' },
        loadChildren: () => import('./student-attendence/student-attendence.module').then(m => m.StudentAttendenceModule),
      },
      {
        path: 'class-home-work',
        data: { pageTitle: 'manageitApp.classHomeWork.home.title' },
        loadChildren: () => import('./class-home-work/class-home-work.module').then(m => m.ClassHomeWorkModule),
      },
      {
        path: 'class-class-work',
        data: { pageTitle: 'manageitApp.classClassWork.home.title' },
        loadChildren: () => import('./class-class-work/class-class-work.module').then(m => m.ClassClassWorkModule),
      },
      {
        path: 'class-lession-plan',
        data: { pageTitle: 'manageitApp.classLessionPlan.home.title' },
        loadChildren: () => import('./class-lession-plan/class-lession-plan.module').then(m => m.ClassLessionPlanModule),
      },
      {
        path: 'class-lession-plan-track',
        data: { pageTitle: 'manageitApp.classLessionPlanTrack.home.title' },
        loadChildren: () => import('./class-lession-plan-track/class-lession-plan-track.module').then(m => m.ClassLessionPlanTrackModule),
      },
      {
        path: 'student-home-work-track',
        data: { pageTitle: 'manageitApp.studentHomeWorkTrack.home.title' },
        loadChildren: () => import('./student-home-work-track/student-home-work-track.module').then(m => m.StudentHomeWorkTrackModule),
      },
      {
        path: 'student-class-work-track',
        data: { pageTitle: 'manageitApp.studentClassWorkTrack.home.title' },
        loadChildren: () => import('./student-class-work-track/student-class-work-track.module').then(m => m.StudentClassWorkTrackModule),
      },
      {
        path: 'class-subject',
        data: { pageTitle: 'manageitApp.classSubject.home.title' },
        loadChildren: () => import('./class-subject/class-subject.module').then(m => m.ClassSubjectModule),
      },
      {
        path: 'subject-chapter',
        data: { pageTitle: 'manageitApp.subjectChapter.home.title' },
        loadChildren: () => import('./subject-chapter/subject-chapter.module').then(m => m.SubjectChapterModule),
      },
      {
        path: 'chapter-section',
        data: { pageTitle: 'manageitApp.chapterSection.home.title' },
        loadChildren: () => import('./chapter-section/chapter-section.module').then(m => m.ChapterSectionModule),
      },
      {
        path: 'student-charges-summary',
        data: { pageTitle: 'manageitApp.studentChargesSummary.home.title' },
        loadChildren: () => import('./student-charges-summary/student-charges-summary.module').then(m => m.StudentChargesSummaryModule),
      },
      {
        path: 'vendors',
        data: { pageTitle: 'manageitApp.vendors.home.title' },
        loadChildren: () => import('./vendors/vendors.module').then(m => m.VendorsModule),
      },
      {
        path: 'income-expenses',
        data: { pageTitle: 'manageitApp.incomeExpenses.home.title' },
        loadChildren: () => import('./income-expenses/income-expenses.module').then(m => m.IncomeExpensesModule),
      },
      {
        path: 'st-income-expenses',
        data: { pageTitle: 'manageitApp.incomeExpenses.home.title' },
        loadChildren: () => import('./st-income-expenses/st-income-expenses.module').then(m => m.STIncomeExpensesModule),
      },
      {
        path: 'st-route',
        data: { pageTitle: 'manageitApp.incomeExpenses.home.title' },
        loadChildren: () => import('./st-route/st-route.module').then(m => m.STRouteModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'manageitApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'manageitApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'question-type',
        data: { pageTitle: 'manageitApp.questionType.home.title' },
        loadChildren: () => import('./question-type/question-type.module').then(m => m.QuestionTypeModule),
      },
      {
        path: 'question',
        data: { pageTitle: 'manageitApp.question.home.title' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      {
        path: 'question-paper',
        data: { pageTitle: 'manageitApp.questionPaper.home.title' },
        loadChildren: () => import('./question-paper/question-paper.module').then(m => m.QuestionPaperModule),
      },
      {
        path: 'tenant',
        data: { pageTitle: 'manageitApp.tenant.home.title' },
        loadChildren: () => import('./tenant/tenant.module').then(m => m.TenantModule),
      },

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

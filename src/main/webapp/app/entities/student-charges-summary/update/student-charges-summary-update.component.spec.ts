jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentChargesSummaryService } from '../service/student-charges-summary.service';
import { IStudentChargesSummary, StudentChargesSummary } from '../student-charges-summary.model';
import { ISchoolLedgerHead } from 'app/entities/school-ledger-head/school-ledger-head.model';
import { SchoolLedgerHeadService } from 'app/entities/school-ledger-head/service/school-ledger-head.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

import { StudentChargesSummaryUpdateComponent } from './student-charges-summary-update.component';

describe('Component Tests', () => {
  describe('StudentChargesSummary Management Update Component', () => {
    let comp: StudentChargesSummaryUpdateComponent;
    let fixture: ComponentFixture<StudentChargesSummaryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentChargesSummaryService: StudentChargesSummaryService;
    let schoolLedgerHeadService: SchoolLedgerHeadService;
    let classStudentService: ClassStudentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentChargesSummaryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentChargesSummaryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentChargesSummaryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentChargesSummaryService = TestBed.inject(StudentChargesSummaryService);
      schoolLedgerHeadService = TestBed.inject(SchoolLedgerHeadService);
      classStudentService = TestBed.inject(ClassStudentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolLedgerHead query and add missing value', () => {
        const studentChargesSummary: IStudentChargesSummary = { id: 456 };
        const schoolLedgerHead: ISchoolLedgerHead = { id: 65084 };
        studentChargesSummary.schoolLedgerHead = schoolLedgerHead;

        const schoolLedgerHeadCollection: ISchoolLedgerHead[] = [{ id: 73051 }];
        spyOn(schoolLedgerHeadService, 'query').and.returnValue(of(new HttpResponse({ body: schoolLedgerHeadCollection })));
        const additionalSchoolLedgerHeads = [schoolLedgerHead];
        const expectedCollection: ISchoolLedgerHead[] = [...additionalSchoolLedgerHeads, ...schoolLedgerHeadCollection];
        spyOn(schoolLedgerHeadService, 'addSchoolLedgerHeadToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentChargesSummary });
        comp.ngOnInit();

        expect(schoolLedgerHeadService.query).toHaveBeenCalled();
        expect(schoolLedgerHeadService.addSchoolLedgerHeadToCollectionIfMissing).toHaveBeenCalledWith(
          schoolLedgerHeadCollection,
          ...additionalSchoolLedgerHeads
        );
        expect(comp.schoolLedgerHeadsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ClassStudent query and add missing value', () => {
        const studentChargesSummary: IStudentChargesSummary = { id: 456 };
        const classStudent: IClassStudent = { id: 23346 };
        studentChargesSummary.classStudent = classStudent;

        const classStudentCollection: IClassStudent[] = [{ id: 79070 }];
        spyOn(classStudentService, 'query').and.returnValue(of(new HttpResponse({ body: classStudentCollection })));
        const additionalClassStudents = [classStudent];
        const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
        spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentChargesSummary });
        comp.ngOnInit();

        expect(classStudentService.query).toHaveBeenCalled();
        expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
          classStudentCollection,
          ...additionalClassStudents
        );
        expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const studentChargesSummary: IStudentChargesSummary = { id: 456 };
        const schoolLedgerHead: ISchoolLedgerHead = { id: 17595 };
        studentChargesSummary.schoolLedgerHead = schoolLedgerHead;
        const classStudent: IClassStudent = { id: 85037 };
        studentChargesSummary.classStudent = classStudent;

        activatedRoute.data = of({ studentChargesSummary });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(studentChargesSummary));
        expect(comp.schoolLedgerHeadsSharedCollection).toContain(schoolLedgerHead);
        expect(comp.classStudentsSharedCollection).toContain(classStudent);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentChargesSummary = { id: 123 };
        spyOn(studentChargesSummaryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentChargesSummary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentChargesSummary }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentChargesSummaryService.update).toHaveBeenCalledWith(studentChargesSummary);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentChargesSummary = new StudentChargesSummary();
        spyOn(studentChargesSummaryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentChargesSummary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentChargesSummary }));
        saveSubject.complete();

        // THEN
        expect(studentChargesSummaryService.create).toHaveBeenCalledWith(studentChargesSummary);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentChargesSummary = { id: 123 };
        spyOn(studentChargesSummaryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentChargesSummary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentChargesSummaryService.update).toHaveBeenCalledWith(studentChargesSummary);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSchoolLedgerHeadById', () => {
        it('Should return tracked SchoolLedgerHead primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolLedgerHeadById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClassStudentById', () => {
        it('Should return tracked ClassStudent primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassStudentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

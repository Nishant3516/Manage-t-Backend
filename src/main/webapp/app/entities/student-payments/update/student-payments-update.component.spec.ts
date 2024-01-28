import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StudentPaymentsService } from '../service/student-payments.service';
import { IStudentPayments, StudentPayments } from '../student-payments.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';

import { StudentPaymentsUpdateComponent } from './student-payments-update.component';

describe('StudentPayments Management Update Component', () => {
  let comp: StudentPaymentsUpdateComponent;
  let fixture: ComponentFixture<StudentPaymentsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studentPaymentsService: StudentPaymentsService;
  let classStudentService: ClassStudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StudentPaymentsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(StudentPaymentsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentPaymentsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    studentPaymentsService = TestBed.inject(StudentPaymentsService);
    classStudentService = TestBed.inject(ClassStudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ClassStudent query and add missing value', () => {
      const studentPayments: IStudentPayments = { id: 456 };
      const classStudent: IClassStudent = { id: 62246 };
      studentPayments.classStudent = classStudent;

      const classStudentCollection: IClassStudent[] = [{ id: 85504 }];
      jest.spyOn(classStudentService, 'query').mockReturnValue(of(new HttpResponse({ body: classStudentCollection })));
      const additionalClassStudents = [classStudent];
      const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
      jest.spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ studentPayments });
      comp.ngOnInit();

      expect(classStudentService.query).toHaveBeenCalled();
      expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
        classStudentCollection,
        ...additionalClassStudents
      );
      expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const studentPayments: IStudentPayments = { id: 456 };
      const classStudent: IClassStudent = { id: 96365 };
      studentPayments.classStudent = classStudent;

      activatedRoute.data = of({ studentPayments });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(studentPayments));
      expect(comp.classStudentsSharedCollection).toContain(classStudent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StudentPayments>>();
      const studentPayments = { id: 123 };
      jest.spyOn(studentPaymentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentPayments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentPayments }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(studentPaymentsService.update).toHaveBeenCalledWith(studentPayments);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StudentPayments>>();
      const studentPayments = new StudentPayments();
      jest.spyOn(studentPaymentsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentPayments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentPayments }));
      saveSubject.complete();

      // THEN
      expect(studentPaymentsService.create).toHaveBeenCalledWith(studentPayments);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StudentPayments>>();
      const studentPayments = { id: 123 };
      jest.spyOn(studentPaymentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentPayments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(studentPaymentsService.update).toHaveBeenCalledWith(studentPayments);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClassStudentById', () => {
      it('Should return tracked ClassStudent primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClassStudentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

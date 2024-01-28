jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentClassWorkTrackService } from '../service/student-class-work-track.service';
import { IStudentClassWorkTrack, StudentClassWorkTrack } from '../student-class-work-track.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { IClassClassWork } from 'app/entities/class-class-work/class-class-work.model';
import { ClassClassWorkService } from 'app/entities/class-class-work/service/class-class-work.service';

import { StudentClassWorkTrackUpdateComponent } from './student-class-work-track-update.component';

describe('Component Tests', () => {
  describe('StudentClassWorkTrack Management Update Component', () => {
    let comp: StudentClassWorkTrackUpdateComponent;
    let fixture: ComponentFixture<StudentClassWorkTrackUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentClassWorkTrackService: StudentClassWorkTrackService;
    let classStudentService: ClassStudentService;
    let classClassWorkService: ClassClassWorkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentClassWorkTrackUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentClassWorkTrackUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentClassWorkTrackUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentClassWorkTrackService = TestBed.inject(StudentClassWorkTrackService);
      classStudentService = TestBed.inject(ClassStudentService);
      classClassWorkService = TestBed.inject(ClassClassWorkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ClassStudent query and add missing value', () => {
        const studentClassWorkTrack: IStudentClassWorkTrack = { id: 456 };
        const classStudent: IClassStudent = { id: 60711 };
        studentClassWorkTrack.classStudent = classStudent;

        const classStudentCollection: IClassStudent[] = [{ id: 57445 }];
        spyOn(classStudentService, 'query').and.returnValue(of(new HttpResponse({ body: classStudentCollection })));
        const additionalClassStudents = [classStudent];
        const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
        spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentClassWorkTrack });
        comp.ngOnInit();

        expect(classStudentService.query).toHaveBeenCalled();
        expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
          classStudentCollection,
          ...additionalClassStudents
        );
        expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ClassClassWork query and add missing value', () => {
        const studentClassWorkTrack: IStudentClassWorkTrack = { id: 456 };
        const classClassWork: IClassClassWork = { id: 33337 };
        studentClassWorkTrack.classClassWork = classClassWork;

        const classClassWorkCollection: IClassClassWork[] = [{ id: 56434 }];
        spyOn(classClassWorkService, 'query').and.returnValue(of(new HttpResponse({ body: classClassWorkCollection })));
        const additionalClassClassWorks = [classClassWork];
        const expectedCollection: IClassClassWork[] = [...additionalClassClassWorks, ...classClassWorkCollection];
        spyOn(classClassWorkService, 'addClassClassWorkToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentClassWorkTrack });
        comp.ngOnInit();

        expect(classClassWorkService.query).toHaveBeenCalled();
        expect(classClassWorkService.addClassClassWorkToCollectionIfMissing).toHaveBeenCalledWith(
          classClassWorkCollection,
          ...additionalClassClassWorks
        );
        expect(comp.classClassWorksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const studentClassWorkTrack: IStudentClassWorkTrack = { id: 456 };
        const classStudent: IClassStudent = { id: 26369 };
        studentClassWorkTrack.classStudent = classStudent;
        const classClassWork: IClassClassWork = { id: 67430 };
        studentClassWorkTrack.classClassWork = classClassWork;

        activatedRoute.data = of({ studentClassWorkTrack });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(studentClassWorkTrack));
        expect(comp.classStudentsSharedCollection).toContain(classStudent);
        expect(comp.classClassWorksSharedCollection).toContain(classClassWork);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentClassWorkTrack = { id: 123 };
        spyOn(studentClassWorkTrackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentClassWorkTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentClassWorkTrack }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentClassWorkTrackService.update).toHaveBeenCalledWith(studentClassWorkTrack);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentClassWorkTrack = new StudentClassWorkTrack();
        spyOn(studentClassWorkTrackService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentClassWorkTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentClassWorkTrack }));
        saveSubject.complete();

        // THEN
        expect(studentClassWorkTrackService.create).toHaveBeenCalledWith(studentClassWorkTrack);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentClassWorkTrack = { id: 123 };
        spyOn(studentClassWorkTrackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentClassWorkTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentClassWorkTrackService.update).toHaveBeenCalledWith(studentClassWorkTrack);
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

      describe('trackClassClassWorkById', () => {
        it('Should return tracked ClassClassWork primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassClassWorkById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

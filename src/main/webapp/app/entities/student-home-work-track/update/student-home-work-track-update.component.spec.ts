jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentHomeWorkTrackService } from '../service/student-home-work-track.service';
import { IStudentHomeWorkTrack, StudentHomeWorkTrack } from '../student-home-work-track.model';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { IClassHomeWork } from 'app/entities/class-home-work/class-home-work.model';
import { ClassHomeWorkService } from 'app/entities/class-home-work/service/class-home-work.service';

import { StudentHomeWorkTrackUpdateComponent } from './student-home-work-track-update.component';

describe('Component Tests', () => {
  describe('StudentHomeWorkTrack Management Update Component', () => {
    let comp: StudentHomeWorkTrackUpdateComponent;
    let fixture: ComponentFixture<StudentHomeWorkTrackUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentHomeWorkTrackService: StudentHomeWorkTrackService;
    let classStudentService: ClassStudentService;
    let classHomeWorkService: ClassHomeWorkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentHomeWorkTrackUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentHomeWorkTrackUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentHomeWorkTrackUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentHomeWorkTrackService = TestBed.inject(StudentHomeWorkTrackService);
      classStudentService = TestBed.inject(ClassStudentService);
      classHomeWorkService = TestBed.inject(ClassHomeWorkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ClassStudent query and add missing value', () => {
        const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 456 };
        const classStudent: IClassStudent = { id: 22926 };
        studentHomeWorkTrack.classStudent = classStudent;

        const classStudentCollection: IClassStudent[] = [{ id: 19454 }];
        spyOn(classStudentService, 'query').and.returnValue(of(new HttpResponse({ body: classStudentCollection })));
        const additionalClassStudents = [classStudent];
        const expectedCollection: IClassStudent[] = [...additionalClassStudents, ...classStudentCollection];
        spyOn(classStudentService, 'addClassStudentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentHomeWorkTrack });
        comp.ngOnInit();

        expect(classStudentService.query).toHaveBeenCalled();
        expect(classStudentService.addClassStudentToCollectionIfMissing).toHaveBeenCalledWith(
          classStudentCollection,
          ...additionalClassStudents
        );
        expect(comp.classStudentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ClassHomeWork query and add missing value', () => {
        const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 456 };
        const classHomeWork: IClassHomeWork = { id: 39200 };
        studentHomeWorkTrack.classHomeWork = classHomeWork;

        const classHomeWorkCollection: IClassHomeWork[] = [{ id: 78656 }];
        spyOn(classHomeWorkService, 'query').and.returnValue(of(new HttpResponse({ body: classHomeWorkCollection })));
        const additionalClassHomeWorks = [classHomeWork];
        const expectedCollection: IClassHomeWork[] = [...additionalClassHomeWorks, ...classHomeWorkCollection];
        spyOn(classHomeWorkService, 'addClassHomeWorkToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ studentHomeWorkTrack });
        comp.ngOnInit();

        expect(classHomeWorkService.query).toHaveBeenCalled();
        expect(classHomeWorkService.addClassHomeWorkToCollectionIfMissing).toHaveBeenCalledWith(
          classHomeWorkCollection,
          ...additionalClassHomeWorks
        );
        expect(comp.classHomeWorksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const studentHomeWorkTrack: IStudentHomeWorkTrack = { id: 456 };
        const classStudent: IClassStudent = { id: 48973 };
        studentHomeWorkTrack.classStudent = classStudent;
        const classHomeWork: IClassHomeWork = { id: 1111 };
        studentHomeWorkTrack.classHomeWork = classHomeWork;

        activatedRoute.data = of({ studentHomeWorkTrack });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(studentHomeWorkTrack));
        expect(comp.classStudentsSharedCollection).toContain(classStudent);
        expect(comp.classHomeWorksSharedCollection).toContain(classHomeWork);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentHomeWorkTrack = { id: 123 };
        spyOn(studentHomeWorkTrackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentHomeWorkTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentHomeWorkTrack }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentHomeWorkTrackService.update).toHaveBeenCalledWith(studentHomeWorkTrack);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentHomeWorkTrack = new StudentHomeWorkTrack();
        spyOn(studentHomeWorkTrackService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentHomeWorkTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: studentHomeWorkTrack }));
        saveSubject.complete();

        // THEN
        expect(studentHomeWorkTrackService.create).toHaveBeenCalledWith(studentHomeWorkTrack);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const studentHomeWorkTrack = { id: 123 };
        spyOn(studentHomeWorkTrackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ studentHomeWorkTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentHomeWorkTrackService.update).toHaveBeenCalledWith(studentHomeWorkTrack);
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

      describe('trackClassHomeWorkById', () => {
        it('Should return tracked ClassHomeWork primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassHomeWorkById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

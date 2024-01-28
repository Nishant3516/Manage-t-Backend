jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SubjectChapterService } from '../service/subject-chapter.service';
import { ISubjectChapter, SubjectChapter } from '../subject-chapter.model';
import { IClassSubject } from 'app/entities/class-subject/class-subject.model';
import { ClassSubjectService } from 'app/entities/class-subject/service/class-subject.service';

import { SubjectChapterUpdateComponent } from './subject-chapter-update.component';

describe('Component Tests', () => {
  describe('SubjectChapter Management Update Component', () => {
    let comp: SubjectChapterUpdateComponent;
    let fixture: ComponentFixture<SubjectChapterUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let subjectChapterService: SubjectChapterService;
    let classSubjectService: ClassSubjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SubjectChapterUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SubjectChapterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubjectChapterUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      subjectChapterService = TestBed.inject(SubjectChapterService);
      classSubjectService = TestBed.inject(ClassSubjectService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ClassSubject query and add missing value', () => {
        const subjectChapter: ISubjectChapter = { id: 456 };
        const classSubject: IClassSubject = { id: 53451 };
        subjectChapter.classSubject = classSubject;

        const classSubjectCollection: IClassSubject[] = [{ id: 25923 }];
        spyOn(classSubjectService, 'query').and.returnValue(of(new HttpResponse({ body: classSubjectCollection })));
        const additionalClassSubjects = [classSubject];
        const expectedCollection: IClassSubject[] = [...additionalClassSubjects, ...classSubjectCollection];
        spyOn(classSubjectService, 'addClassSubjectToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ subjectChapter });
        comp.ngOnInit();

        expect(classSubjectService.query).toHaveBeenCalled();
        expect(classSubjectService.addClassSubjectToCollectionIfMissing).toHaveBeenCalledWith(
          classSubjectCollection,
          ...additionalClassSubjects
        );
        expect(comp.classSubjectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const subjectChapter: ISubjectChapter = { id: 456 };
        const classSubject: IClassSubject = { id: 86714 };
        subjectChapter.classSubject = classSubject;

        activatedRoute.data = of({ subjectChapter });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(subjectChapter));
        expect(comp.classSubjectsSharedCollection).toContain(classSubject);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subjectChapter = { id: 123 };
        spyOn(subjectChapterService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subjectChapter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subjectChapter }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(subjectChapterService.update).toHaveBeenCalledWith(subjectChapter);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subjectChapter = new SubjectChapter();
        spyOn(subjectChapterService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subjectChapter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subjectChapter }));
        saveSubject.complete();

        // THEN
        expect(subjectChapterService.create).toHaveBeenCalledWith(subjectChapter);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subjectChapter = { id: 123 };
        spyOn(subjectChapterService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subjectChapter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(subjectChapterService.update).toHaveBeenCalledWith(subjectChapter);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClassSubjectById', () => {
        it('Should return tracked ClassSubject primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassSubjectById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

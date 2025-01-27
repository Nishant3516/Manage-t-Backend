jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SchoolEventService } from '../service/school-event.service';
import { ISchoolEvent, SchoolEvent } from '../school-event.model';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

import { SchoolEventUpdateComponent } from './school-event-update.component';

describe('Component Tests', () => {
  describe('SchoolEvent Management Update Component', () => {
    let comp: SchoolEventUpdateComponent;
    let fixture: ComponentFixture<SchoolEventUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let schoolEventService: SchoolEventService;
    let schoolClassService: SchoolClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolEventUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SchoolEventUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolEventUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      schoolEventService = TestBed.inject(SchoolEventService);
      schoolClassService = TestBed.inject(SchoolClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SchoolClass query and add missing value', () => {
        const schoolEvent: ISchoolEvent = { id: 456 };
        const schoolClasses: ISchoolClass[] = [{ id: 17718 }];
        schoolEvent.schoolClasses = schoolClasses;

        const schoolClassCollection: ISchoolClass[] = [{ id: 42118 }];
        spyOn(schoolClassService, 'query').and.returnValue(of(new HttpResponse({ body: schoolClassCollection })));
        const additionalSchoolClasses = [...schoolClasses];
        const expectedCollection: ISchoolClass[] = [...additionalSchoolClasses, ...schoolClassCollection];
        spyOn(schoolClassService, 'addSchoolClassToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ schoolEvent });
        comp.ngOnInit();

        expect(schoolClassService.query).toHaveBeenCalled();
        expect(schoolClassService.addSchoolClassToCollectionIfMissing).toHaveBeenCalledWith(
          schoolClassCollection,
          ...additionalSchoolClasses
        );
        expect(comp.schoolClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const schoolEvent: ISchoolEvent = { id: 456 };
        const schoolClasses: ISchoolClass = { id: 82913 };
        schoolEvent.schoolClasses = [schoolClasses];

        activatedRoute.data = of({ schoolEvent });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(schoolEvent));
        expect(comp.schoolClassesSharedCollection).toContain(schoolClasses);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolEvent = { id: 123 };
        spyOn(schoolEventService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolEvent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolEvent }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(schoolEventService.update).toHaveBeenCalledWith(schoolEvent);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolEvent = new SchoolEvent();
        spyOn(schoolEventService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolEvent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolEvent }));
        saveSubject.complete();

        // THEN
        expect(schoolEventService.create).toHaveBeenCalledWith(schoolEvent);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolEvent = { id: 123 };
        spyOn(schoolEventService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolEvent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(schoolEventService.update).toHaveBeenCalledWith(schoolEvent);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSchoolClassById', () => {
        it('Should return tracked SchoolClass primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolClassById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedSchoolClass', () => {
        it('Should return option if no SchoolClass is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedSchoolClass(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected SchoolClass for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedSchoolClass(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this SchoolClass is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedSchoolClass(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});

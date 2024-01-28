jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassLessionPlanTrackService } from '../service/class-lession-plan-track.service';
import { IClassLessionPlanTrack, ClassLessionPlanTrack } from '../class-lession-plan-track.model';
import { IClassLessionPlan } from 'app/entities/class-lession-plan/class-lession-plan.model';
import { ClassLessionPlanService } from 'app/entities/class-lession-plan/service/class-lession-plan.service';

import { ClassLessionPlanTrackUpdateComponent } from './class-lession-plan-track-update.component';

describe('Component Tests', () => {
  describe('ClassLessionPlanTrack Management Update Component', () => {
    let comp: ClassLessionPlanTrackUpdateComponent;
    let fixture: ComponentFixture<ClassLessionPlanTrackUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classLessionPlanTrackService: ClassLessionPlanTrackService;
    let classLessionPlanService: ClassLessionPlanService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassLessionPlanTrackUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassLessionPlanTrackUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassLessionPlanTrackUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classLessionPlanTrackService = TestBed.inject(ClassLessionPlanTrackService);
      classLessionPlanService = TestBed.inject(ClassLessionPlanService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ClassLessionPlan query and add missing value', () => {
        const classLessionPlanTrack: IClassLessionPlanTrack = { id: 456 };
        const classLessionPlan: IClassLessionPlan = { id: 33614 };
        classLessionPlanTrack.classLessionPlan = classLessionPlan;

        const classLessionPlanCollection: IClassLessionPlan[] = [{ id: 24731 }];
        spyOn(classLessionPlanService, 'query').and.returnValue(of(new HttpResponse({ body: classLessionPlanCollection })));
        const additionalClassLessionPlans = [classLessionPlan];
        const expectedCollection: IClassLessionPlan[] = [...additionalClassLessionPlans, ...classLessionPlanCollection];
        spyOn(classLessionPlanService, 'addClassLessionPlanToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classLessionPlanTrack });
        comp.ngOnInit();

        expect(classLessionPlanService.query).toHaveBeenCalled();
        expect(classLessionPlanService.addClassLessionPlanToCollectionIfMissing).toHaveBeenCalledWith(
          classLessionPlanCollection,
          ...additionalClassLessionPlans
        );
        expect(comp.classLessionPlansSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classLessionPlanTrack: IClassLessionPlanTrack = { id: 456 };
        const classLessionPlan: IClassLessionPlan = { id: 6887 };
        classLessionPlanTrack.classLessionPlan = classLessionPlan;

        activatedRoute.data = of({ classLessionPlanTrack });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classLessionPlanTrack));
        expect(comp.classLessionPlansSharedCollection).toContain(classLessionPlan);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classLessionPlanTrack = { id: 123 };
        spyOn(classLessionPlanTrackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classLessionPlanTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classLessionPlanTrack }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classLessionPlanTrackService.update).toHaveBeenCalledWith(classLessionPlanTrack);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classLessionPlanTrack = new ClassLessionPlanTrack();
        spyOn(classLessionPlanTrackService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classLessionPlanTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classLessionPlanTrack }));
        saveSubject.complete();

        // THEN
        expect(classLessionPlanTrackService.create).toHaveBeenCalledWith(classLessionPlanTrack);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classLessionPlanTrack = { id: 123 };
        spyOn(classLessionPlanTrackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classLessionPlanTrack });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classLessionPlanTrackService.update).toHaveBeenCalledWith(classLessionPlanTrack);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClassLessionPlanById', () => {
        it('Should return tracked ClassLessionPlan primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassLessionPlanById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IdStoreService } from '../service/id-store.service';
import { IIdStore, IdStore } from '../id-store.model';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';

import { IdStoreUpdateComponent } from './id-store-update.component';

describe('Component Tests', () => {
  describe('IdStore Management Update Component', () => {
    let comp: IdStoreUpdateComponent;
    let fixture: ComponentFixture<IdStoreUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let idStoreService: IdStoreService;
    let schoolService: SchoolService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IdStoreUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IdStoreUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IdStoreUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      idStoreService = TestBed.inject(IdStoreService);
      schoolService = TestBed.inject(SchoolService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call School query and add missing value', () => {
        const idStore: IIdStore = { id: 456 };
        const school: ISchool = { id: 60909 };
        idStore.school = school;

        const schoolCollection: ISchool[] = [{ id: 86710 }];
        spyOn(schoolService, 'query').and.returnValue(of(new HttpResponse({ body: schoolCollection })));
        const additionalSchools = [school];
        const expectedCollection: ISchool[] = [...additionalSchools, ...schoolCollection];
        spyOn(schoolService, 'addSchoolToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ idStore });
        comp.ngOnInit();

        expect(schoolService.query).toHaveBeenCalled();
        expect(schoolService.addSchoolToCollectionIfMissing).toHaveBeenCalledWith(schoolCollection, ...additionalSchools);
        expect(comp.schoolsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const idStore: IIdStore = { id: 456 };
        const school: ISchool = { id: 61843 };
        idStore.school = school;

        activatedRoute.data = of({ idStore });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(idStore));
        expect(comp.schoolsSharedCollection).toContain(school);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const idStore = { id: 123 };
        spyOn(idStoreService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ idStore });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: idStore }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(idStoreService.update).toHaveBeenCalledWith(idStore);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const idStore = new IdStore();
        spyOn(idStoreService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ idStore });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: idStore }));
        saveSubject.complete();

        // THEN
        expect(idStoreService.create).toHaveBeenCalledWith(idStore);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const idStore = { id: 123 };
        spyOn(idStoreService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ idStore });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(idStoreService.update).toHaveBeenCalledWith(idStore);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSchoolById', () => {
        it('Should return tracked School primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

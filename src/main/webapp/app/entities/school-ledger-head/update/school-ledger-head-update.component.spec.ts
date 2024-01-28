jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SchoolLedgerHeadService } from '../service/school-ledger-head.service';
import { ISchoolLedgerHead, SchoolLedgerHead } from '../school-ledger-head.model';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';

import { SchoolLedgerHeadUpdateComponent } from './school-ledger-head-update.component';

describe('Component Tests', () => {
  describe('SchoolLedgerHead Management Update Component', () => {
    let comp: SchoolLedgerHeadUpdateComponent;
    let fixture: ComponentFixture<SchoolLedgerHeadUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let schoolLedgerHeadService: SchoolLedgerHeadService;
    let schoolService: SchoolService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SchoolLedgerHeadUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SchoolLedgerHeadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolLedgerHeadUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      schoolLedgerHeadService = TestBed.inject(SchoolLedgerHeadService);
      schoolService = TestBed.inject(SchoolService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call School query and add missing value', () => {
        const schoolLedgerHead: ISchoolLedgerHead = { id: 456 };
        const school: ISchool = { id: 46292 };
        schoolLedgerHead.school = school;

        const schoolCollection: ISchool[] = [{ id: 82564 }];
        spyOn(schoolService, 'query').and.returnValue(of(new HttpResponse({ body: schoolCollection })));
        const additionalSchools = [school];
        const expectedCollection: ISchool[] = [...additionalSchools, ...schoolCollection];
        spyOn(schoolService, 'addSchoolToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ schoolLedgerHead });
        comp.ngOnInit();

        expect(schoolService.query).toHaveBeenCalled();
        expect(schoolService.addSchoolToCollectionIfMissing).toHaveBeenCalledWith(schoolCollection, ...additionalSchools);
        expect(comp.schoolsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const schoolLedgerHead: ISchoolLedgerHead = { id: 456 };
        const school: ISchool = { id: 60148 };
        schoolLedgerHead.school = school;

        activatedRoute.data = of({ schoolLedgerHead });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(schoolLedgerHead));
        expect(comp.schoolsSharedCollection).toContain(school);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolLedgerHead = { id: 123 };
        spyOn(schoolLedgerHeadService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolLedgerHead });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolLedgerHead }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(schoolLedgerHeadService.update).toHaveBeenCalledWith(schoolLedgerHead);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolLedgerHead = new SchoolLedgerHead();
        spyOn(schoolLedgerHeadService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolLedgerHead });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: schoolLedgerHead }));
        saveSubject.complete();

        // THEN
        expect(schoolLedgerHeadService.create).toHaveBeenCalledWith(schoolLedgerHead);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const schoolLedgerHead = { id: 123 };
        spyOn(schoolLedgerHeadService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ schoolLedgerHead });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(schoolLedgerHeadService.update).toHaveBeenCalledWith(schoolLedgerHead);
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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AuditLogService } from '../service/audit-log.service';
import { IAuditLog, AuditLog } from '../audit-log.model';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';
import { ISchoolUser } from 'app/entities/school-user/school-user.model';
import { SchoolUserService } from 'app/entities/school-user/service/school-user.service';

import { AuditLogUpdateComponent } from './audit-log-update.component';

describe('Component Tests', () => {
  describe('AuditLog Management Update Component', () => {
    let comp: AuditLogUpdateComponent;
    let fixture: ComponentFixture<AuditLogUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let auditLogService: AuditLogService;
    let schoolService: SchoolService;
    let schoolUserService: SchoolUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AuditLogUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AuditLogUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AuditLogUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      auditLogService = TestBed.inject(AuditLogService);
      schoolService = TestBed.inject(SchoolService);
      schoolUserService = TestBed.inject(SchoolUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call School query and add missing value', () => {
        const auditLog: IAuditLog = { id: 456 };
        const school: ISchool = { id: 52623 };
        auditLog.school = school;

        const schoolCollection: ISchool[] = [{ id: 96570 }];
        spyOn(schoolService, 'query').and.returnValue(of(new HttpResponse({ body: schoolCollection })));
        const additionalSchools = [school];
        const expectedCollection: ISchool[] = [...additionalSchools, ...schoolCollection];
        spyOn(schoolService, 'addSchoolToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ auditLog });
        comp.ngOnInit();

        expect(schoolService.query).toHaveBeenCalled();
        expect(schoolService.addSchoolToCollectionIfMissing).toHaveBeenCalledWith(schoolCollection, ...additionalSchools);
        expect(comp.schoolsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SchoolUser query and add missing value', () => {
        const auditLog: IAuditLog = { id: 456 };
        const schoolUser: ISchoolUser = { id: 46838 };
        auditLog.schoolUser = schoolUser;

        const schoolUserCollection: ISchoolUser[] = [{ id: 53571 }];
        spyOn(schoolUserService, 'query').and.returnValue(of(new HttpResponse({ body: schoolUserCollection })));
        const additionalSchoolUsers = [schoolUser];
        const expectedCollection: ISchoolUser[] = [...additionalSchoolUsers, ...schoolUserCollection];
        spyOn(schoolUserService, 'addSchoolUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ auditLog });
        comp.ngOnInit();

        expect(schoolUserService.query).toHaveBeenCalled();
        expect(schoolUserService.addSchoolUserToCollectionIfMissing).toHaveBeenCalledWith(schoolUserCollection, ...additionalSchoolUsers);
        expect(comp.schoolUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const auditLog: IAuditLog = { id: 456 };
        const school: ISchool = { id: 44872 };
        auditLog.school = school;
        const schoolUser: ISchoolUser = { id: 42719 };
        auditLog.schoolUser = schoolUser;

        activatedRoute.data = of({ auditLog });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(auditLog));
        expect(comp.schoolsSharedCollection).toContain(school);
        expect(comp.schoolUsersSharedCollection).toContain(schoolUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const auditLog = { id: 123 };
        spyOn(auditLogService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ auditLog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: auditLog }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(auditLogService.update).toHaveBeenCalledWith(auditLog);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const auditLog = new AuditLog();
        spyOn(auditLogService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ auditLog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: auditLog }));
        saveSubject.complete();

        // THEN
        expect(auditLogService.create).toHaveBeenCalledWith(auditLog);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const auditLog = { id: 123 };
        spyOn(auditLogService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ auditLog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(auditLogService.update).toHaveBeenCalledWith(auditLog);
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

      describe('trackSchoolUserById', () => {
        it('Should return tracked SchoolUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSchoolUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

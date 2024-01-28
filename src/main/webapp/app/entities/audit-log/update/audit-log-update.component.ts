import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAuditLog, AuditLog } from '../audit-log.model';
import { AuditLogService } from '../service/audit-log.service';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';
import { ISchoolUser } from 'app/entities/school-user/school-user.model';
import { SchoolUserService } from 'app/entities/school-user/service/school-user.service';

@Component({
  selector: 'jhi-audit-log-update',
  templateUrl: './audit-log-update.component.html',
})
export class AuditLogUpdateComponent implements OnInit {
  isSaving = false;

  schoolsSharedCollection: ISchool[] = [];
  schoolUsersSharedCollection: ISchoolUser[] = [];

  editForm = this.fb.group({
    id: [],
    userName: [null, [Validators.required]],
    userDeviceDetails: [null, [Validators.required]],
    action: [],
    data1: [],
    data2: [],
    data3: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    school: [],
    schoolUser: [],
  });

  constructor(
    protected auditLogService: AuditLogService,
    protected schoolService: SchoolService,
    protected schoolUserService: SchoolUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auditLog }) => {
      this.updateForm(auditLog);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const auditLog = this.createFromForm();
    if (auditLog.id !== undefined) {
      this.subscribeToSaveResponse(this.auditLogService.update(auditLog));
    } else {
      this.subscribeToSaveResponse(this.auditLogService.create(auditLog));
    }
  }

  trackSchoolById(index: number, item: ISchool): number {
    return item.id!;
  }

  trackSchoolUserById(index: number, item: ISchoolUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuditLog>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(auditLog: IAuditLog): void {
    this.editForm.patchValue({
      id: auditLog.id,
      userName: auditLog.userName,
      userDeviceDetails: auditLog.userDeviceDetails,
      action: auditLog.action,
      data1: auditLog.data1,
      data2: auditLog.data2,
      data3: auditLog.data3,
      createDate: auditLog.createDate,
      lastModified: auditLog.lastModified,
      cancelDate: auditLog.cancelDate,
      school: auditLog.school,
      schoolUser: auditLog.schoolUser,
    });

    this.schoolsSharedCollection = this.schoolService.addSchoolToCollectionIfMissing(this.schoolsSharedCollection, auditLog.school);
    this.schoolUsersSharedCollection = this.schoolUserService.addSchoolUserToCollectionIfMissing(
      this.schoolUsersSharedCollection,
      auditLog.schoolUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.schoolService
      .query()
      .pipe(map((res: HttpResponse<ISchool[]>) => res.body ?? []))
      .pipe(map((schools: ISchool[]) => this.schoolService.addSchoolToCollectionIfMissing(schools, this.editForm.get('school')!.value)))
      .subscribe((schools: ISchool[]) => (this.schoolsSharedCollection = schools));

    this.schoolUserService
      .query()
      .pipe(map((res: HttpResponse<ISchoolUser[]>) => res.body ?? []))
      .pipe(
        map((schoolUsers: ISchoolUser[]) =>
          this.schoolUserService.addSchoolUserToCollectionIfMissing(schoolUsers, this.editForm.get('schoolUser')!.value)
        )
      )
      .subscribe((schoolUsers: ISchoolUser[]) => (this.schoolUsersSharedCollection = schoolUsers));
  }

  protected createFromForm(): IAuditLog {
    return {
      ...new AuditLog(),
      id: this.editForm.get(['id'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      userDeviceDetails: this.editForm.get(['userDeviceDetails'])!.value,
      action: this.editForm.get(['action'])!.value,
      data1: this.editForm.get(['data1'])!.value,
      data2: this.editForm.get(['data2'])!.value,
      data3: this.editForm.get(['data3'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      school: this.editForm.get(['school'])!.value,
      schoolUser: this.editForm.get(['schoolUser'])!.value,
    };
  }
}

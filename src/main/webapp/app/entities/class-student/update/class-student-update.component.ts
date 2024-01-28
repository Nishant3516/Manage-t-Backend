import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IClassStudent, ClassStudent } from '../class-student.model';
import { ClassStudentService } from '../service/class-student.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';

@Component({
  selector: 'jhi-class-student-update',
  templateUrl: './class-student-update.component.html',
})
export class ClassStudentUpdateComponent implements OnInit {
  isSaving = false;

  schoolClassesSharedCollection: ISchoolClass[] = [];

  editForm = this.fb.group({
    id: [],
    studentPhoto: [],
    studentPhotoContentType: [],
    studentPhotoLink: [],
    studentId: [],
    firstName: [null, [Validators.required]],
    gender: [],
    lastName: [],
    rollNumber: [],
    phoneNumber: [],
    bloodGroup: [],
    dateOfBirth: [],
    startDate: [],
    addressLine1: [],
    addressLine2: [],
    nickName: [],
    fatherName: [],
    motherName: [],
    email: [],
    admissionDate: [],
    regNumber: [],
    endDate: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    schoolClass: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected classStudentService: ClassStudentService,
    protected schoolClassService: SchoolClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classStudent }) => {
      if (classStudent.id === undefined) {
        const today = dayjs().startOf('day');
        classStudent.dateOfBirth = today;
      }

      this.updateForm(classStudent);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('manageitApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classStudent = this.createFromForm();
    if (classStudent.id !== undefined) {
      this.subscribeToSaveResponse(this.classStudentService.update(classStudent));
    } else {
      this.subscribeToSaveResponse(this.classStudentService.create(classStudent));
    }
  }

  trackSchoolClassById(index: number, item: ISchoolClass): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassStudent>>): void {
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

  protected updateForm(classStudent: IClassStudent): void {
    this.editForm.patchValue({
      id: classStudent.id,
      studentPhoto: classStudent.studentPhoto,
      studentPhotoContentType: classStudent.studentPhotoContentType,
      studentPhotoLink: classStudent.studentPhotoLink,
      studentId: classStudent.studentId,
      firstName: classStudent.firstName,
      gender: classStudent.gender,
      lastName: classStudent.lastName,
      rollNumber: classStudent.rollNumber,
      phoneNumber: classStudent.phoneNumber,
      bloodGroup: classStudent.bloodGroup,
      dateOfBirth: classStudent.dateOfBirth ? classStudent.dateOfBirth.format(DATE_TIME_FORMAT) : null,
      startDate: classStudent.startDate,
      addressLine1: classStudent.addressLine1,
      addressLine2: classStudent.addressLine2,
      nickName: classStudent.nickName,
      fatherName: classStudent.fatherName,
      motherName: classStudent.motherName,
      email: classStudent.email,
      admissionDate: classStudent.admissionDate,
      regNumber: classStudent.regNumber,
      endDate: classStudent.endDate,
      createDate: classStudent.createDate,
      lastModified: classStudent.lastModified,
      cancelDate: classStudent.cancelDate,
      schoolClass: classStudent.schoolClass,
    });

    this.schoolClassesSharedCollection = this.schoolClassService.addSchoolClassToCollectionIfMissing(
      this.schoolClassesSharedCollection,
      classStudent.schoolClass
    );
  }

  protected loadRelationshipsOptions(): void {
    this.schoolClassService
      .query()
      .pipe(map((res: HttpResponse<ISchoolClass[]>) => res.body ?? []))
      .pipe(
        map((schoolClasses: ISchoolClass[]) =>
          this.schoolClassService.addSchoolClassToCollectionIfMissing(schoolClasses, this.editForm.get('schoolClass')!.value)
        )
      )
      .subscribe((schoolClasses: ISchoolClass[]) => (this.schoolClassesSharedCollection = schoolClasses));
  }

  protected createFromForm(): IClassStudent {
    return {
      ...new ClassStudent(),
      id: this.editForm.get(['id'])!.value,
      studentPhotoContentType: this.editForm.get(['studentPhotoContentType'])!.value,
      studentPhoto: this.editForm.get(['studentPhoto'])!.value,
      studentPhotoLink: this.editForm.get(['studentPhotoLink'])!.value,
      studentId: this.editForm.get(['studentId'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      rollNumber: this.editForm.get(['rollNumber'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      bloodGroup: this.editForm.get(['bloodGroup'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value
        ? dayjs(this.editForm.get(['dateOfBirth'])!.value, DATE_TIME_FORMAT)
        : undefined,
      startDate: this.editForm.get(['startDate'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      nickName: this.editForm.get(['nickName'])!.value,
      fatherName: this.editForm.get(['fatherName'])!.value,
      motherName: this.editForm.get(['motherName'])!.value,
      email: this.editForm.get(['email'])!.value,
      admissionDate: this.editForm.get(['admissionDate'])!.value,
      regNumber: this.editForm.get(['regNumber'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      schoolClass: this.editForm.get(['schoolClass'])!.value,
    };
  }
}

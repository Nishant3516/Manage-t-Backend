import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentClassWorkTrack, StudentClassWorkTrack } from '../student-class-work-track.model';
import { StudentClassWorkTrackService } from '../service/student-class-work-track.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { IClassClassWork } from 'app/entities/class-class-work/class-class-work.model';
import { ClassClassWorkService } from 'app/entities/class-class-work/service/class-class-work.service';

@Component({
  selector: 'jhi-student-class-work-track-update',
  templateUrl: './student-class-work-track-update.component.html',
})
export class StudentClassWorkTrackUpdateComponent implements OnInit {
  isSaving = false;

  classStudentsSharedCollection: IClassStudent[] = [];
  classClassWorksSharedCollection: IClassClassWork[] = [];

  editForm = this.fb.group({
    id: [],
    workStatus: [null, [Validators.required]],
    remarks: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    classStudent: [],
    classClassWork: [],
  });

  constructor(
    protected studentClassWorkTrackService: StudentClassWorkTrackService,
    protected classStudentService: ClassStudentService,
    protected classClassWorkService: ClassClassWorkService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentClassWorkTrack }) => {
      this.updateForm(studentClassWorkTrack);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentClassWorkTrack = this.createFromForm();
    if (studentClassWorkTrack.id !== undefined) {
      this.subscribeToSaveResponse(this.studentClassWorkTrackService.update(studentClassWorkTrack));
    } else {
      this.subscribeToSaveResponse(this.studentClassWorkTrackService.create(studentClassWorkTrack));
    }
  }

  trackClassStudentById(index: number, item: IClassStudent): number {
    return item.id!;
  }

  trackClassClassWorkById(index: number, item: IClassClassWork): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentClassWorkTrack>>): void {
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

  protected updateForm(studentClassWorkTrack: IStudentClassWorkTrack): void {
    this.editForm.patchValue({
      id: studentClassWorkTrack.id,
      workStatus: studentClassWorkTrack.workStatus,
      remarks: studentClassWorkTrack.remarks,
      createDate: studentClassWorkTrack.createDate,
      lastModified: studentClassWorkTrack.lastModified,
      cancelDate: studentClassWorkTrack.cancelDate,
      classStudent: studentClassWorkTrack.classStudent,
      classClassWork: studentClassWorkTrack.classClassWork,
    });

    this.classStudentsSharedCollection = this.classStudentService.addClassStudentToCollectionIfMissing(
      this.classStudentsSharedCollection,
      studentClassWorkTrack.classStudent
    );
    this.classClassWorksSharedCollection = this.classClassWorkService.addClassClassWorkToCollectionIfMissing(
      this.classClassWorksSharedCollection,
      studentClassWorkTrack.classClassWork
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classStudentService
      .query()
      .pipe(map((res: HttpResponse<IClassStudent[]>) => res.body ?? []))
      .pipe(
        map((classStudents: IClassStudent[]) =>
          this.classStudentService.addClassStudentToCollectionIfMissing(classStudents, this.editForm.get('classStudent')!.value)
        )
      )
      .subscribe((classStudents: IClassStudent[]) => (this.classStudentsSharedCollection = classStudents));

    this.classClassWorkService
      .query()
      .pipe(map((res: HttpResponse<IClassClassWork[]>) => res.body ?? []))
      .pipe(
        map((classClassWorks: IClassClassWork[]) =>
          this.classClassWorkService.addClassClassWorkToCollectionIfMissing(classClassWorks, this.editForm.get('classClassWork')!.value)
        )
      )
      .subscribe((classClassWorks: IClassClassWork[]) => (this.classClassWorksSharedCollection = classClassWorks));
  }

  protected createFromForm(): IStudentClassWorkTrack {
    return {
      ...new StudentClassWorkTrack(),
      id: this.editForm.get(['id'])!.value,
      workStatus: this.editForm.get(['workStatus'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      classStudent: this.editForm.get(['classStudent'])!.value,
      classClassWork: this.editForm.get(['classClassWork'])!.value,
    };
  }
}

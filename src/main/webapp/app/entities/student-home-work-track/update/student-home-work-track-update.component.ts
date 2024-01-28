import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudentHomeWorkTrack, StudentHomeWorkTrack } from '../student-home-work-track.model';
import { StudentHomeWorkTrackService } from '../service/student-home-work-track.service';
import { IClassStudent } from 'app/entities/class-student/class-student.model';
import { ClassStudentService } from 'app/entities/class-student/service/class-student.service';
import { IClassHomeWork } from 'app/entities/class-home-work/class-home-work.model';
import { ClassHomeWorkService } from 'app/entities/class-home-work/service/class-home-work.service';

@Component({
  selector: 'jhi-student-home-work-track-update',
  templateUrl: './student-home-work-track-update.component.html',
})
export class StudentHomeWorkTrackUpdateComponent implements OnInit {
  isSaving = false;

  classStudentsSharedCollection: IClassStudent[] = [];
  classHomeWorksSharedCollection: IClassHomeWork[] = [];

  editForm = this.fb.group({
    id: [],
    workStatus: [null, [Validators.required]],
    remarks: [],
    createDate: [],
    lastModified: [],
    cancelDate: [],
    classStudent: [],
    classHomeWork: [],
  });

  constructor(
    protected studentHomeWorkTrackService: StudentHomeWorkTrackService,
    protected classStudentService: ClassStudentService,
    protected classHomeWorkService: ClassHomeWorkService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentHomeWorkTrack }) => {
      this.updateForm(studentHomeWorkTrack);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentHomeWorkTrack = this.createFromForm();
    if (studentHomeWorkTrack.id !== undefined) {
      this.subscribeToSaveResponse(this.studentHomeWorkTrackService.update(studentHomeWorkTrack));
    } else {
      this.subscribeToSaveResponse(this.studentHomeWorkTrackService.create(studentHomeWorkTrack));
    }
  }

  trackClassStudentById(index: number, item: IClassStudent): number {
    return item.id!;
  }

  trackClassHomeWorkById(index: number, item: IClassHomeWork): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentHomeWorkTrack>>): void {
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

  protected updateForm(studentHomeWorkTrack: IStudentHomeWorkTrack): void {
    this.editForm.patchValue({
      id: studentHomeWorkTrack.id,
      workStatus: studentHomeWorkTrack.workStatus,
      remarks: studentHomeWorkTrack.remarks,
      createDate: studentHomeWorkTrack.createDate,
      lastModified: studentHomeWorkTrack.lastModified,
      cancelDate: studentHomeWorkTrack.cancelDate,
      classStudent: studentHomeWorkTrack.classStudent,
      classHomeWork: studentHomeWorkTrack.classHomeWork,
    });

    this.classStudentsSharedCollection = this.classStudentService.addClassStudentToCollectionIfMissing(
      this.classStudentsSharedCollection,
      studentHomeWorkTrack.classStudent
    );
    this.classHomeWorksSharedCollection = this.classHomeWorkService.addClassHomeWorkToCollectionIfMissing(
      this.classHomeWorksSharedCollection,
      studentHomeWorkTrack.classHomeWork
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

    this.classHomeWorkService
      .query()
      .pipe(map((res: HttpResponse<IClassHomeWork[]>) => res.body ?? []))
      .pipe(
        map((classHomeWorks: IClassHomeWork[]) =>
          this.classHomeWorkService.addClassHomeWorkToCollectionIfMissing(classHomeWorks, this.editForm.get('classHomeWork')!.value)
        )
      )
      .subscribe((classHomeWorks: IClassHomeWork[]) => (this.classHomeWorksSharedCollection = classHomeWorks));
  }

  protected createFromForm(): IStudentHomeWorkTrack {
    return {
      ...new StudentHomeWorkTrack(),
      id: this.editForm.get(['id'])!.value,
      workStatus: this.editForm.get(['workStatus'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      createDate: this.editForm.get(['createDate'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value,
      cancelDate: this.editForm.get(['cancelDate'])!.value,
      classStudent: this.editForm.get(['classStudent'])!.value,
      classHomeWork: this.editForm.get(['classHomeWork'])!.value,
    };
  }
}

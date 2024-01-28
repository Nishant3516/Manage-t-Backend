import { ISchoolClass } from 'app/entities/school-class/school-class.model';
import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { HttpResponse } from '@angular/common/http';

export abstract class SchoolClassFilter {
  selectedClassId: any;
  schoolClasses: ISchoolClass[] = [];
  isLoading = false;

  constructor(protected schoolClassService: SchoolClassService) {}
  loadAllClasses(): void {
    this.isLoading = true;

    this.schoolClassService.query().subscribe(
      (res: HttpResponse<ISchoolClass[]>) => {
        this.isLoading = false;
        this.schoolClasses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  trackFilterClassById(index: number, item: ISchoolClass): number {
    return item.id!;
  }
}

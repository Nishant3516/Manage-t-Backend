import { SchoolClassService } from 'app/entities/school-class/service/school-class.service';
import { HttpResponse } from '@angular/common/http';
import { SchoolClassFilter } from './class.filter';
import { IClassStudent } from '../../entities/class-student/class-student.model';
import { ClassStudentService } from '../../entities/class-student/service/class-student.service';

export abstract class StudentClassAndStudentFilter extends SchoolClassFilter {
  selectedStudentId: any;
  isLoading = false;
  classStudentsForAClass: IClassStudent[] = [];

  constructor(protected schoolClassService: SchoolClassService, protected classStudentService: ClassStudentService) {
    super(schoolClassService);
  }

  loadAllStudentsForAClass(): void {
    this.isLoading = true;
    this.classStudentsForAClass = [];
    this.classStudentService.query({ 'schoolClassId.equals': this.selectedClassId, size: 60 }).subscribe(
      (res: HttpResponse<IClassStudent[]>) => {
        this.isLoading = false;
        this.classStudentsForAClass = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  trackFilterStudentId(index: number, item: IClassStudent): number {
    return item.id!;
  }
}

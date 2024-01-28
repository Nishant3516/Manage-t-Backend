import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolPictureGallery } from '../school-picture-gallery.model';
import { SchoolPictureGalleryService } from '../service/school-picture-gallery.service';

@Component({
  templateUrl: './school-picture-gallery-delete-dialog.component.html',
})
export class SchoolPictureGalleryDeleteDialogComponent {
  schoolPictureGallery?: ISchoolPictureGallery;

  constructor(protected schoolPictureGalleryService: SchoolPictureGalleryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolPictureGalleryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

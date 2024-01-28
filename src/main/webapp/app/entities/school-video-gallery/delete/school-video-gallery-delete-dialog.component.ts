import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchoolVideoGallery } from '../school-video-gallery.model';
import { SchoolVideoGalleryService } from '../service/school-video-gallery.service';

@Component({
  templateUrl: './school-video-gallery-delete-dialog.component.html',
})
export class SchoolVideoGalleryDeleteDialogComponent {
  schoolVideoGallery?: ISchoolVideoGallery;

  constructor(protected schoolVideoGalleryService: SchoolVideoGalleryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schoolVideoGalleryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

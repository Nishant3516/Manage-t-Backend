import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolPictureGallery } from '../school-picture-gallery.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-school-picture-gallery-detail',
  templateUrl: './school-picture-gallery-detail.component.html',
})
export class SchoolPictureGalleryDetailComponent implements OnInit {
  schoolPictureGallery: ISchoolPictureGallery | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schoolPictureGallery }) => {
      this.schoolPictureGallery = schoolPictureGallery;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { ClassClassWorkDetailComponent } from './class-class-work-detail.component';

describe('Component Tests', () => {
  describe('ClassClassWork Management Detail Component', () => {
    let comp: ClassClassWorkDetailComponent;
    let fixture: ComponentFixture<ClassClassWorkDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ClassClassWorkDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ classClassWork: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ClassClassWorkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassClassWorkDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load classClassWork on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classClassWork).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});

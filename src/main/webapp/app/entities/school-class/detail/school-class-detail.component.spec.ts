import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SchoolClassDetailComponent } from './school-class-detail.component';

describe('Component Tests', () => {
  describe('SchoolClass Management Detail Component', () => {
    let comp: SchoolClassDetailComponent;
    let fixture: ComponentFixture<SchoolClassDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SchoolClassDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ schoolClass: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SchoolClassDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolClassDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load schoolClass on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.schoolClass).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

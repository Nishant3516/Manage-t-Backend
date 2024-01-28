import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SchoolDaysOffDetailComponent } from './school-days-off-detail.component';

describe('Component Tests', () => {
  describe('SchoolDaysOff Management Detail Component', () => {
    let comp: SchoolDaysOffDetailComponent;
    let fixture: ComponentFixture<SchoolDaysOffDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SchoolDaysOffDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ schoolDaysOff: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SchoolDaysOffDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolDaysOffDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load schoolDaysOff on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.schoolDaysOff).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

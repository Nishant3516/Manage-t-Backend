import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SchoolEventDetailComponent } from './school-event-detail.component';

describe('Component Tests', () => {
  describe('SchoolEvent Management Detail Component', () => {
    let comp: SchoolEventDetailComponent;
    let fixture: ComponentFixture<SchoolEventDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SchoolEventDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ schoolEvent: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SchoolEventDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolEventDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load schoolEvent on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.schoolEvent).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

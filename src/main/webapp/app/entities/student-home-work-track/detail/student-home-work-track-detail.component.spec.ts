import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentHomeWorkTrackDetailComponent } from './student-home-work-track-detail.component';

describe('Component Tests', () => {
  describe('StudentHomeWorkTrack Management Detail Component', () => {
    let comp: StudentHomeWorkTrackDetailComponent;
    let fixture: ComponentFixture<StudentHomeWorkTrackDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentHomeWorkTrackDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentHomeWorkTrack: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentHomeWorkTrackDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentHomeWorkTrackDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentHomeWorkTrack on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentHomeWorkTrack).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

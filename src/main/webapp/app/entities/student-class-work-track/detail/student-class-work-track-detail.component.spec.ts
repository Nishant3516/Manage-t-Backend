import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentClassWorkTrackDetailComponent } from './student-class-work-track-detail.component';

describe('Component Tests', () => {
  describe('StudentClassWorkTrack Management Detail Component', () => {
    let comp: StudentClassWorkTrackDetailComponent;
    let fixture: ComponentFixture<StudentClassWorkTrackDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentClassWorkTrackDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentClassWorkTrack: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentClassWorkTrackDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentClassWorkTrackDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentClassWorkTrack on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentClassWorkTrack).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassLessionPlanTrackDetailComponent } from './class-lession-plan-track-detail.component';

describe('Component Tests', () => {
  describe('ClassLessionPlanTrack Management Detail Component', () => {
    let comp: ClassLessionPlanTrackDetailComponent;
    let fixture: ComponentFixture<ClassLessionPlanTrackDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ClassLessionPlanTrackDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ classLessionPlanTrack: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ClassLessionPlanTrackDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassLessionPlanTrackDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load classLessionPlanTrack on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classLessionPlanTrack).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

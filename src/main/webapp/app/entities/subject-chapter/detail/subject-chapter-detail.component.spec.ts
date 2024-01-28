import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubjectChapterDetailComponent } from './subject-chapter-detail.component';

describe('Component Tests', () => {
  describe('SubjectChapter Management Detail Component', () => {
    let comp: SubjectChapterDetailComponent;
    let fixture: ComponentFixture<SubjectChapterDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SubjectChapterDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ subjectChapter: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SubjectChapterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubjectChapterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load subjectChapter on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subjectChapter).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

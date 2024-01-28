import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChapterSectionDetailComponent } from './chapter-section-detail.component';

describe('Component Tests', () => {
  describe('ChapterSection Management Detail Component', () => {
    let comp: ChapterSectionDetailComponent;
    let fixture: ComponentFixture<ChapterSectionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChapterSectionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chapterSection: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChapterSectionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChapterSectionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chapterSection on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chapterSection).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

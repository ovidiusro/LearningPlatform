import { TestBed, inject } from '@angular/core/testing';

import { HttpLessonService } from './http-lesson.service';

describe('HttpLessonService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpLessonService]
    });
  });

  it('should be created', inject([HttpLessonService], (service: HttpLessonService) => {
    expect(service).toBeTruthy();
  }));
});

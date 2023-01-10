import { TestBed, inject } from '@angular/core/testing';

import { HttpGroupService } from './http-group.service';

describe('HttpGroupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpGroupService]
    });
  });

  it('should be created', inject([HttpGroupService], (service: HttpGroupService) => {
    expect(service).toBeTruthy();
  }));
});

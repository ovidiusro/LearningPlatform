import { TestBed, inject } from '@angular/core/testing';

import { HttpAccountService } from './httpAccount.service';

describe('HttpAccountService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpAccountService]
    });
  });

  it('should be created', inject([HttpAccountService], (service: HttpAccountService) => {
    expect(service).toBeTruthy();
  }));
});

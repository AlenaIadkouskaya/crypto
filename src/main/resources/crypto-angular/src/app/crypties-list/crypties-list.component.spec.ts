import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CryptiesListComponent } from './crypties-list.component';

describe('CryptiesListComponent', () => {
  let component: CryptiesListComponent;
  let fixture: ComponentFixture<CryptiesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CryptiesListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CryptiesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

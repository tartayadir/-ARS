import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComeBackButtonComponent } from './come-back-button.component';

describe('ComeBackButtonComponent', () => {
  let component: ComeBackButtonComponent;
  let fixture: ComponentFixture<ComeBackButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComeBackButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComeBackButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

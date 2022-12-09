import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDeleteWindowComponent } from './confirm-delete-window.component';

describe('ConfirmDeleteWindowComponent', () => {
  let component: ConfirmDeleteWindowComponent;
  let fixture: ComponentFixture<ConfirmDeleteWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmDeleteWindowComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmDeleteWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

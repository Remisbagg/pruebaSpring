import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FixedSidebarRightComponent } from './fixed-sidebar-right.component';

describe('FixedSidebarRightComponent', () => {
  let component: FixedSidebarRightComponent;
  let fixture: ComponentFixture<FixedSidebarRightComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FixedSidebarRightComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FixedSidebarRightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

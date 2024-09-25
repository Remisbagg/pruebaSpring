import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FixedSidebarLeftComponent } from './fixed-sidebar-left.component';

describe('FixedSidebarLeftComponent', () => {
  let component: FixedSidebarLeftComponent;
  let fixture: ComponentFixture<FixedSidebarLeftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FixedSidebarLeftComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FixedSidebarLeftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

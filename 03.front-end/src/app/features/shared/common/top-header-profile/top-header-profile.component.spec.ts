import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopHeaderProfileComponent } from './top-header-profile.component';

describe('TopHeaderProfileComponent', () => {
  let component: TopHeaderProfileComponent;
  let fixture: ComponentFixture<TopHeaderProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopHeaderProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopHeaderProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

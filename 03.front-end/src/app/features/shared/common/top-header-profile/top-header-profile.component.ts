import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-top-header-profile',
  templateUrl: './top-header-profile.component.html',
  styleUrls: ['./top-header-profile.component.css']
})
export class TopHeaderProfileComponent implements OnInit{
  @Input() userId:any
  @Input() avatar:any;
  @Input() user:any;

  constructor() {
  }

  ngOnInit(): void {
  }

}

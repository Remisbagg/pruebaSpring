import {Component, Input} from '@angular/core';
import {NgxMasonryOptions} from "ngx-masonry";

@Component({
  selector: 'app-post-images',
  templateUrl: './post-images.component.html',
  styleUrls: ['./post-images.component.css']
})
export class PostImagesComponent {
  @Input() images!: any;
  masonryOptions: NgxMasonryOptions = {
    originLeft: true,
    resize: true,
    originTop:true,
  };

}

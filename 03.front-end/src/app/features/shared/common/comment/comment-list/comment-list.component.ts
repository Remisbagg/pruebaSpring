import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent implements OnInit {
  @Input() comments: any;
  slicedComments!: any
  count = 1;
  size = 3

  ngOnInit(): void {
    this.slicedComments = this.comments.slice(-(this.count * this.size));
  }

  loadMoreComments() {
    this.count +=1 ;
    this.slicedComments = this.comments.slice(-(this.count * this.size));
  }
}

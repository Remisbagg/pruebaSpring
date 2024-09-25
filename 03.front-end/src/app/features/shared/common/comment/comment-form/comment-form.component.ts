import {Component, Input, OnInit} from '@angular/core';
import {PostCommentService} from "../../../../../service/comment/post-comment.service";
import {SweetAlertService} from "../../../../../service/alert/sweet-alert.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.css']
})
export class CommentFormComponent implements OnInit {
  commentForm: any;
  @Input() userId: any;
  @Input() postId!: any;
  @Input() avatar!: any;

  constructor(private postCommentService: PostCommentService,
              private alertService: SweetAlertService) {
  }

  ngOnInit(): void {
    this.initializeCommentForm();
  }

  submitComment(userId: any, postId: any) {
    const comment = this.commentForm.value;
    this.postCommentService.postComment(userId, postId, comment).subscribe({
      next: () => {
        this.commentForm.reset();
      },
      error: (data: any) => {
        const text = JSON.stringify(data.error.error)
        this.alertService.error("Error", text);
      }
    })
  }

  initializeCommentForm() {
    this.commentForm = new FormGroup({
      content: new FormControl()
    })
  }
}

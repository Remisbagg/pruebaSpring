import {Component, EventEmitter, Input, Output} from '@angular/core';
import {PostLikeService} from "../../../../service/postlike/post-like.service";
import {SweetAlertService} from "../../../../service/alert/sweet-alert.service";

@Component({
  selector: 'app-post-additional-info',
  templateUrl: './post-additional-info.component.html',
  styleUrls: ['./post-additional-info.component.css']
})
export class PostAdditionalInfoComponent {
  @Input() userId!: any;
  @Input() postUserId!: any;
  @Input() postId!: any;
  @Input() post!: any;
  @Output() postLike = new EventEmitter();

  constructor(private postLikeService: PostLikeService,
              private alertService: SweetAlertService) {
  }

  checkLiked(likes: any) {
    if (likes.length != 0) {
      for (let l of likes) {
        if (l.userId == this.userId) {
          return true;
        }
      }
    }
    return false;
  }

  reactPost(userId: any, postId: any) {
    this.postLikeService.likeOrUnlikePost(userId, postId).subscribe({
      next: () => {
        this.postLike.emit();
      },
      error: (data: any) => {
        const text = JSON.stringify(data.error.error)
        this.alertService.error("Error", text);
      }
    })
  }
}

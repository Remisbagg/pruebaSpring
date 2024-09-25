import {Component, OnInit} from '@angular/core';
import {TokenUtils} from "../../shared/utils/token.utils";
import {PostService} from "../../../service/post/post.service";
import {FormControl, FormGroup} from "@angular/forms";
import {ProfileService} from "../../../service/profile/profile.service";
import {PostCommentService} from "../../../service/comment/post-comment.service";
import {WebsocketService} from "../../../service/websocket/websocket.service";
import {PostLikeService} from "../../../service/postlike/post-like.service";
import {SweetAlertService} from "../../../service/alert/sweet-alert.service";

@Component({
  selector: 'app-newsfeed-item',
  templateUrl: './newsfeed-item.component.html',
  styleUrls: ['./newsfeed-item.component.css']
})
export class NewsfeedItemComponent implements OnInit {
  userId!: any;
  posts!: any;
  commentForm!: any;
  user!: any;

  constructor(private postService: PostService,
              private userService: ProfileService,
              private postCommentService: PostCommentService,
              private postLikeService: PostLikeService,
              private websocketService: WebsocketService,
              private alertService: SweetAlertService) {
  }

  ngOnInit() {
    const token = sessionStorage.getItem('access_token');
    if (token) {
      const payload = TokenUtils.parseJwt(token);
      this.userId = payload.id;
      this.getNewsfeed()
      this.connectWebSocket()
    }
    this.initializeCommentForm();
    this.getCurrentUser(this.userId);
  }

  getNewsfeed() {
    this.postService.getNewsfeed(this.userId).subscribe({
      next: (data: any) => {
        this.posts = data.data.content;
      },
      error: (data: any) => {
        const text = JSON.stringify(data.error.error);
        this.alertService.error("Error", text);
      }
    })
  }

  initializeCommentForm() {
    this.commentForm = new FormGroup({
      content: new FormControl()
    })
  }

  getCurrentUser(id: any) {
    this.userService.getCurrentUser(id).subscribe({
      next: (data: any) => {
        this.user = data.data;
      },
      error: (data: any) => {
        const text = JSON.stringify(data.error.error)
        this.alertService.error("Error", text);
      }
    })
  }

  connectWebSocket() {
    this.getNewsfeedUpdate();
    this.getNewComment();
    this.getNewLike();
  }

  private getNewsfeedUpdate() {
    const topic = `/topic/user.${this.userId}.newPost`;
    this.websocketService.subscribeToTopic(topic, (message) => {
      this.getNewsfeed();
    }).then(() => {
    })
  }

  private getNewComment() {
    const topic = `/topic/user.${this.userId}.newComment`
    this.websocketService.subscribeToTopic(topic, (message) => {
      this.getNewsfeed();
    }).then(() => {
    })
  }

  private getNewLike() {
    const topic = `/topic/user.${this.userId}.newPostLike`;
    this.websocketService.subscribeToTopic(topic, (message) => {
      this.getNewsfeed();
    }).then(() => {
    })
  }
}

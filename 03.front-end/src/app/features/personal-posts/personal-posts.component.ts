import {Component, OnInit} from '@angular/core';
import {PostService} from "../../service/post/post.service";
import {ProfileService} from "../../service/profile/profile.service";
import {TokenUtils} from "../shared/utils/token.utils";
import Swal from "sweetalert2";
import {FormControl, FormGroup} from "@angular/forms";
import {PostCommentService} from "../../service/comment/post-comment.service";
import {WebsocketService} from "../../service/websocket/websocket.service";
import {SweetAlertService} from "../../service/alert/sweet-alert.service";
import {FriendshipService} from "../../service/friendship/friendship.service";


@Component({
  selector: 'app-personal-posts',
  templateUrl: './personal-posts.component.html',
  styleUrls: ['./personal-posts.component.css']
})
export class PersonalPostsComponent implements OnInit {
  posts!: any;
  userId!: any;
  commentForm!: any;
  user!: any;
  friendList!: any;

  constructor(private postService: PostService,
              private userService: ProfileService,
              private postCommentService: PostCommentService,
              private websocketService: WebsocketService,
              private userProfileService: ProfileService,
              private alertService: SweetAlertService,
              private friendshipService: FriendshipService) {
  }

  ngOnInit(): void {
    const token = sessionStorage.getItem('access_token');
    if (token) {
      const payload = TokenUtils.parseJwt(token);
      this.userId = payload.id;
      this.user = this.getCurrentUser(this.userId);
      this.getPosts();
      this.connectWebSocket();
    }
    this.initializeCommentForm();
    this.getFriendList();
  }

  getPosts() {
    this.postService.getUserPosts(this.userId).subscribe({
      next: (data: any) => {
        this.posts = data.data.content;
      },
      error: (data: any) => {
        const text = JSON.stringify(data.error.error)
        this.alertService.error("Error", text);
      }
    })
  }

  delete(postId: any) {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!"
    }).then((result) => {
      if (result.isConfirmed) {
        this.postService.deletePost(this.userId, postId).subscribe({
          next: () => {
            this.getPosts();
            this.alertService.success("Your post has been deleted")
          },
          error: (data: any) => {
            const text = JSON.stringify(data.error.error);
            this.alertService.error("Error", text);
          }
        })
      }
    });
  }

  initializeCommentForm() {
    this.commentForm = new FormGroup({
      content: new FormControl()
    })
  }

  private getCurrentUser(userId: any) {
    this.userProfileService.getCurrentUser(userId).subscribe({
      next: (data: any) => {
        this.user = data.data;
      },
      error: (data: any) => {
        const text = JSON.stringify(data.error.error)
        this.alertService.error("Error", text);
      }
    })
  }

  private connectWebSocket() {
    this.getNewComment();
    this.getNewLike();
    this.getNewPost();
  }

  private getNewComment() {
    const topic = `/topic/user.${this.userId}.newComment`
    this.websocketService.subscribeToTopic(topic, (message) => {
      this.getPosts();
    }).then(() => {
    })
  }

  private getNewLike() {
    const topic = `/topic/user.${this.userId}.newPostLike`;
    this.websocketService.subscribeToTopic(topic, (message) => {
      this.getPosts();
    }).then(() => {
    })
  }

  private getFriendList() {
    this.friendshipService.getFriendList().subscribe({
      next: (data: any) => {
        console.log(data.data)
        this.friendList = data.data;
      },
      error: (data: any) => {
        const text = JSON.stringify(data.error.error)
        this.alertService.error("Error", text);
      }
    })
  }

  private getNewPost() {
    const topic = `/topic/user.${this.userId}.newPost`;
    this.websocketService.subscribeToTopic(topic, () => {
      this.getPosts();
    }).then(() => {
    })
  }
}

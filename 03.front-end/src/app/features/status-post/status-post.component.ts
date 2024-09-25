import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {PostService} from "../../service/post/post.service";
import {TokenUtils} from "../shared/utils/token.utils";
import {ProfileService} from "../../service/profile/profile.service";
import {FormControl, FormGroup} from "@angular/forms";
import {SweetAlertService} from "../../service/alert/sweet-alert.service";
import {PrivacyEnum} from "../shared/utils/privacy.enum";

declare var $: any;

@Component({
  selector: 'app-status-post',
  templateUrl: './status-post.component.html',
  styleUrls: ['./status-post.component.css']
})
export class StatusPostComponent implements OnInit {
  userId!: any;
  statusForm!: any;
  user!: any;
  files = [];
  images = []
  privacy!: string;
  @ViewChild('modalUploadImages') modalUploadImages!: ElementRef;

  constructor(private postService: PostService,
              private userService: ProfileService,
              private alertService: SweetAlertService) {
  }

  ngOnInit() {
    const token = sessionStorage.getItem('access_token');
    if (token) {
      const payload = TokenUtils.parseJwt(token);
      this.userId = payload.id;
    }
    this.getCurrentUser(this.userId);
    this.initializeStatusForm();
    this.privacy = "PUBLIC"
  }

  getCurrentUser(id: any) {
    this.userService.getCurrentUser(id).subscribe({
      next: (data: any) => {
        this.user = data.data;
      }
    })
  }

  initializeStatusForm() {
    this.statusForm = new FormGroup({
      content: new FormControl()
    })
  }

  submitStatus() {
    const postCreate = {
      content: this.statusForm.controls[('content')].value,
      privacy: this.privacy
    }

    this.postService.createNewPost(this.userId, postCreate, this.files).subscribe({
      next: () => {
        this.clearForm();
        this.alertService.success("New status created")
      },
      error: (data: any) => {
        const text = data.error.error;
        this.alertService.error("Error", text);
      }
    })
  }

  onFilesSelected(event: any) {
    $(this.modalUploadImages.nativeElement).modal('hide');
    this.files = event.target.files;
    if (this.files && this.files.length != 0) {
      for (let file of this.files) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const image = {
            url: e.target.result
          }
          // @ts-ignore
          this.images.push(image);
        }
        reader.readAsDataURL(file);
      }
    }
  }

  clearForm() {
    this.statusForm.reset();
    this.files = [];
    this.images = [];
  }

  onChangePostPrivacy(e: any) {
    this.privacy = e.target.value
  }
}

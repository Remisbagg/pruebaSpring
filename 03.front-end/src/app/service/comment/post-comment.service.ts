import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConstants} from "../../app-constant";

@Injectable({
  providedIn: 'root'
})
export class PostCommentService {

  constructor(private httpClient:HttpClient) { }

  postComment(userId:any, postId:any, comment:any) {
    return this.httpClient.post<any>(AppConstants.BASE_URL_API + `/v1/users/${userId}/posts/${postId}/comments`, comment);
  }
}

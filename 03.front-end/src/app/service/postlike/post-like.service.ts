import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppConstants} from "../../app-constant";

@Injectable({
  providedIn: 'root'
})
export class PostLikeService {

  constructor(private httpClient:HttpClient) { }

  likeOrUnlikePost(userId:any, postId:any) {
    return this.httpClient.post<any>(AppConstants.BASE_URL_API + `/v1/users/${userId}/posts/${postId}`, null)
  }
}

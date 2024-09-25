import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ForgotPasswordComponent} from './features/account/forgot-password/forgot-password.component';
import {ErrorComponent} from './features/error/error.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ResetPasswordComponent} from './features/account/reset-password/reset-password.component';
import {NewsfeedComponent} from './features/newsfeed/newsfeed.component';
import {LoginRegisterComponent} from './features/account/login-register/login-register.component';
import {HeaderComponent} from './features/shared/common/header/header.component';
import {TokenInterceptor} from "./features/shared/auth/token.interceptor";
import {NewsfeedItemComponent} from './features/newsfeed/newsfeed-item/newsfeed-item.component';
import {StatusPostComponent} from './features/status-post/status-post.component';
import {PersonalPostsComponent} from './features/personal-posts/personal-posts.component';
import {FixedSidebarLeftComponent} from './features/shared/common/fixed-sidebar-left/fixed-sidebar-left.component';
import {FixedSidebarRightComponent} from './features/shared/common/fixed-sidebar-right/fixed-sidebar-right.component';
import {BackToTopArrowComponent} from './features/shared/common/back-to-top-arrow/back-to-top-arrow.component';
import {TimeAgoPipe} from "./features/shared/utils/time-ago.pipe";
import {CommentFormComponent} from './features/shared/common/comment/comment-form/comment-form.component';
import {CommentListComponent} from './features/shared/common/comment/comment-list/comment-list.component';
import {
  PostAdditionalInfoComponent
} from './features/shared/common/post-additional-info/post-additional-info.component';
import {PostImagesComponent} from './features/shared/common/post-images/post-images.component';
import {TopHeaderProfileComponent} from './features/shared/common/top-header-profile/top-header-profile.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgxMasonryModule} from "ngx-masonry";
import {LeftSidebarComponent} from './features/personal-posts/left-sidebar/left-sidebar.component';
import {RightSidebarComponent} from './features/personal-posts/right-sidebar/right-sidebar.component';
import {SearchResultComponent} from './features/search-result/search-result.component';

@NgModule({
  declarations: [
    AppComponent,
    ForgotPasswordComponent,
    ErrorComponent,
    ResetPasswordComponent,
    NewsfeedComponent,
    LoginRegisterComponent,
    HeaderComponent,
    NewsfeedItemComponent,
    StatusPostComponent,
    PersonalPostsComponent,
    FixedSidebarLeftComponent,
    FixedSidebarRightComponent,
    BackToTopArrowComponent,
    TimeAgoPipe,
    CommentFormComponent,
    CommentListComponent,
    PostAdditionalInfoComponent,
    PostImagesComponent,
    TopHeaderProfileComponent,
    LeftSidebarComponent,
    RightSidebarComponent,
    SearchResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    NgxMasonryModule,
  ],
  providers: [TokenInterceptor],
  bootstrap: [AppComponent]
})
export class AppModule {
}

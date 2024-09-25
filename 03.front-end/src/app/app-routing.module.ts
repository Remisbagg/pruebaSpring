import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ForgotPasswordComponent} from "./features/account/forgot-password/forgot-password.component";
import {ErrorComponent} from "./features/error/error.component";
import {ResetPasswordComponent} from "./features/account/reset-password/reset-password.component";
import {NewsfeedComponent} from "./features/newsfeed/newsfeed.component";
import {LoginRegisterComponent} from "./features/account/login-register/login-register.component";
import {AuthorizeGuard} from "./features/shared/auth/Authorize.guard";
import {PersonalPostsComponent} from "./features/personal-posts/personal-posts.component";
import {SearchResultComponent} from "./features/search-result/search-result.component";

const routes: Routes = [
  {path: 'newsfeed', component: NewsfeedComponent, canActivate: [AuthorizeGuard]},
  {path: 'personal-post', component: PersonalPostsComponent, canActivate: [AuthorizeGuard]},
  {path: 'search-result', component: SearchResultComponent, canActivate: [AuthorizeGuard]},
  {path: '', redirectTo: 'newsfeed', pathMatch: 'full'},
  {path: 'home', redirectTo: 'newsfeed', pathMatch: 'full'},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
  {path: 'login', component: LoginRegisterComponent},
  {path: 'register', component: LoginRegisterComponent},
  {path: 'error', component: ErrorComponent},
  {path: '**', component: ErrorComponent}
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

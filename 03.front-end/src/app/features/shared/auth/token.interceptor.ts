import {
  HTTP_INTERCEPTORS, HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {Router} from "@angular/router";
import {TokenUtils} from "../utils/token.utils";
import {catchError, Observable} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = sessionStorage.getItem("access_token");
    if (token) {

      // decode JWT payload part.
      const payload = TokenUtils.parseJwt(token);

      // Check token exp.
      if (!payload || Date.now() >= payload.exp * 1000) {
        // Token expire remove it in sessionStorage
        sessionStorage.removeItem("access_token");
      } else {
        // If we have a token, we set it to the header
        request = this.addToken(request, token)
      }
    }

    // Skip setting 'Content-Type' to 'application/json' for FormData requests
    if (!(request.body instanceof FormData) && !request.headers.has('Content-Type')) {
      request = this.addContentType(request, 'application/json');
    }

    //Redirect HTTP Status Error
    return next.handle(request)
      .pipe(
      catchError((error: HttpErrorResponse) => {
        // if(error.error.code == 500 && error.error.error == "Bad credentials") {
        //   throw error;
        // }
        // else
          if (error.status >= 500 && error.status <= 599) {
          this.router.navigateByUrl("/error").then(r => {})
        } else if (error.status == 401 || error.status == 403) {
          this.router.navigate(['/login']).then(r => {})
        }
        throw error;
      })
    );
  }

  private addContentType(request: HttpRequest<any>, contentType: string) {
    return request.clone({
      setHeaders: {
        'Content-Type': contentType,
      },
    });
  }

  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
}

export const TokenInterceptor = {
  provide: HTTP_INTERCEPTORS,
  useClass: AuthInterceptorService,
  multi: true
};

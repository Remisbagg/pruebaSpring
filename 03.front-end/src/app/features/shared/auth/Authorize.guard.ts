import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {TokenUtils} from "../utils/token.utils";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthorizeGuard implements CanActivate {
  constructor(
    private router: Router
  ) {
  }

  canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): boolean {
    return this.checkAuthorization();
  }

  private checkAuthorization(): boolean {
    const token = sessionStorage.getItem("access_token");
    if (token) {
      const payload = TokenUtils.parseJwt(token); // decode JWT payload part.
      if (!payload || Date.now() >= payload.exp * 1000) { // Check token exp.
        sessionStorage.removeItem("access_token");
        this.router.navigate(['login']);
        return false;
      } else {
        return true;
      }
    }
    // this.router.navigate(['error'],{state:{systemErrorMessage:AppConstants.ERR023_MSG}});
    this.router.navigate(['login']);
    return false;
  }
}

import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpHandler, HttpInterceptor, HttpEvent, HttpRequest, HttpUserEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private router: Router) { }


    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        if (req.headers.get('No-Auth') === 'True') {
            return next.handle(req.clone());
        }

        if (localStorage.getItem('userToken') != null) {
            const clonedreq = req.clone({
                headers: req.headers.set('Authorization', 'Bearer ' + localStorage.getItem('userToken'))
            });
            return next.handle(clonedreq);
        } else {
            this.router.navigateByUrl('/sign-in');
        }

    }

}

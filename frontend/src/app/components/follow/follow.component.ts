import { Component, OnInit } from '@angular/core';
import { HttpAccountService } from 'src/app/services/httpAccount.service';

@Component({
  selector: 'app-follow',
  templateUrl: './follow.component.html',
  styleUrls: ['./follow.component.css']
})
export class FollowComponent implements OnInit {

  groups: any = [];
  initialized = false;
  httpRoot = 'http://localhost:8080/api/files/';

  constructor(private httpAccountService: HttpAccountService) { }


  ngOnInit() {
    const principal = JSON.parse(localStorage.getItem('currentUser'));
    this.groups = this.httpAccountService.getFollowGroups(principal.id).subscribe((res: any) => {
      console.log(res);
      this.groups = res;
      this.initialized = true;
    });
  }

}

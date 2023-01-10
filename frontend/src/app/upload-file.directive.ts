import { Directive, ViewContainerRef, Input, } from '@angular/core';

@Directive({
  selector: '[appUploadFile]'
})
export class UploadFileDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }


  receiveEvent($event) {
    console.log('in directive : ' + $event);
  }


}

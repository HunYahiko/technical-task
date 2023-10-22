import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Post} from "../post.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PostService} from "../post.service";
import {PostData} from "../post-data.model";
import {Observer} from "rxjs";

@Component({
  selector: 'app-add-post-dialog',
  templateUrl: './add-post-dialog.component.html',
  styleUrls: ['./add-post-dialog.component.css']
})
export class AddPostDialogComponent {

  postForm = new FormGroup({
    title: new FormControl('', [Validators.required,
      Validators.minLength(3),
      Validators.maxLength(150)]),
    description: new FormControl('', [Validators.required,
      Validators.minLength(3),
      Validators.maxLength(150)]),
    content: new FormControl('', [Validators.required,
      Validators.minLength(10),
      Validators.maxLength(3000)])
  })

  constructor(
    public dialogRef: MatDialogRef<AddPostDialogComponent>,
    public postService: PostService
  ) {
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  addPost(): void {
    const post: PostData = {
      title: this.postForm.controls['title'].value,
      description: this.postForm.controls['description'].value,
      content: this.postForm.controls['content'].value
    }

    const observer: Observer<any> = {
      next: () => {},
      error: (err: Error) => {
        console.debug(err);
      },
      complete: () => this.dialogRef.close()
    }

    this.postService.addPost(post).subscribe(observer);
  }
}

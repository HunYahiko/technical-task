import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Post} from "../post.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PostData} from "../post-data.model";
import {Observer} from "rxjs";
import {PostService} from "../post.service";

@Component({
  selector: 'app-edit-post-dialog',
  templateUrl: './edit-post-dialog.component.html',
  styleUrls: ['./edit-post-dialog.component.css']
})
export class EditPostDialogComponent {

  postForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<EditPostDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Post,
    public postService: PostService
  ) {
    this.postForm = new FormGroup({
      title: new FormControl(data.title, [Validators.required,
        Validators.minLength(3),
        Validators.maxLength(150)]),
      description: new FormControl(data.description, [Validators.required,
        Validators.minLength(3),
        Validators.maxLength(150)]),
      content: new FormControl(data.content, [Validators.required,
        Validators.minLength(10),
        Validators.maxLength(3000)])
    })
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  editPost(): void {
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

    this.postService.updatePost(post, this.data.id).subscribe(observer);
  }
}

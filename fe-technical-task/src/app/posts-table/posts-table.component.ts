import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {DataSource} from "@angular/cdk/collections";
import {Post} from "../post.model";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {PostService} from "../post.service";
import {catchError, map, merge, startWith, switchMap, of as observableOf, Observer} from "rxjs";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {AddPostDialogComponent} from "../add-post-dialog/add-post-dialog.component";
import {EditPostDialogComponent} from "../edit-post-dialog/edit-post-dialog.component";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-posts-table',
  templateUrl: './posts-table.component.html',
  styleUrls: ['./posts-table.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class PostsTableComponent implements AfterViewInit {
  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;
  // @ts-ignore
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = [
    'title',
    'description',
    'actions'
  ];
  resultsLength = 0;
  isLoadingResults = true;
  // @ts-ignore
  expandedPost: Post | null;

  posts: MatTableDataSource<Post> = new MatTableDataSource<Post>();

  constructor(private postService: PostService,
              private dialog: MatDialog) {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));
    this.loadData();
  }

  openAddPostDialog(): void {
    const dialogRef = this.dialog.open(AddPostDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
    })
  }

  loadData(): void {
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.postService.getPosts(
            this.sort.active,
            this.sort.direction,
            this.paginator.pageSize,
            this.paginator.pageIndex
          ).pipe(catchError(() => observableOf(null)));
        }),
        map(data => {
          this.isLoadingResults = false;

          if (data === null) {
            return [];
          }

          // Only refresh the result length if there is new data. In case of rate
          // limit errors, we do not want to reset the paginator to zero, as that
          // would prevent users from re-triggering requests.
          if (this.resultsLength !== data.totalElements) {
            this.resultsLength = data.totalElements;
          }
          return data;
        }),
      )
      .subscribe(data => {
        if ("content" in data) {
          this.posts.data = data.content;
        }
      });
  }

  removePost(id: number) {
    const observer: Observer<any> = {
      next: () => {},
      error: (err: Error) => {
        alert("Something went wrong while deleting the post!")
      },
      complete: () => {
        this.loadData();
      }
    }
    this.postService.deletePost(id).subscribe(observer);
  }

  editPost(data: Post) {
    const dialogRef = this.dialog.open(EditPostDialogComponent,
      {data: data});

    dialogRef.afterClosed().subscribe(result => {
      this.loadData();
    })
  }
}

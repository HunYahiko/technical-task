import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Post} from "./post.model";
import {PostData} from "./post-data.model";
import {PostPage} from "./post-page.model";
import {SortDirection} from "@angular/material/sort";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private readonly HREF: string = 'http://localhost:8080/posts';

  constructor(private httpClient: HttpClient) {

  }

  public getPosts(sort: string, sortDirection: SortDirection, pageSize: number, pageNumber: number): Observable<PostPage> {
    return this.httpClient.get<PostPage>(`${this.HREF}?sort=${sort},${sortDirection}&size=${pageSize}&page=${pageNumber}`);
  }

  public addPost(data: PostData): Observable<any> {
    return this.httpClient.post(`${this.HREF}`, data);
  }

  public deletePost(id: number): Observable<any> {
    return this.httpClient.delete(`${this.HREF}/${id}`);
  }

  public updatePost(data: PostData, id: number): Observable<any> {
    return this.httpClient.put(`${this.HREF}/${id}`, data);
  }
}

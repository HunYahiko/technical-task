import {Post} from "./post.model";

export interface PostPage {
  content: Post[],
  totalElements: number,
  number: number
}

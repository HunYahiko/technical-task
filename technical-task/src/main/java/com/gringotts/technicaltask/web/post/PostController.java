package com.gringotts.technicaltask.web.post;

import com.gringotts.technicaltask.service.post.PostService;
import com.gringotts.technicaltask.service.postcomment.PostCommentService;
import com.gringotts.technicaltask.web.postcomment.AddCommentCommand;
import com.gringotts.technicaltask.web.postcomment.EditCommentCommand;
import com.gringotts.technicaltask.web.postcomment.PostCommentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@CrossOrigin
class PostController {

    private final PostService postService;
    private final PostCommentService postCommentService;

    @GetMapping
    public ResponseEntity<Page<PostDto>> getPosts(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(postService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePostCommand command) {
        postService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody UpdatePostCommand command,
                                       @PathVariable("id") Long id) {
        command.setId(id);
        postService.update(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<PostCommentDto>> getPostComments(@PathVariable("id") Long postId,
                                                                @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(postCommentService.getAllBy(postId, pageable));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable("id") Long postId,
                                           @RequestBody AddCommentCommand command) {
        command.setPostId(postId);
        postCommentService.add(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> editComment(@PathVariable("postId") Long postId,
                                            @PathVariable("commentId") Long commentId,
                                            @RequestBody EditCommentCommand command) {
        command.setId(commentId);
        postCommentService.edit(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<Void> removeComment(@PathVariable("postId") Long postId,
                                              @PathVariable("commentId") Long commentId) {
        postCommentService.remove(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}

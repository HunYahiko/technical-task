package com.gringotts.technicaltask.service.postcomment;

import com.gringotts.technicaltask.domain.Post;
import com.gringotts.technicaltask.domain.PostComment;
import com.gringotts.technicaltask.exception.EntityNotFoundException;
import com.gringotts.technicaltask.repository.PostCommentRepository;
import com.gringotts.technicaltask.repository.PostRepository;
import com.gringotts.technicaltask.utils.Mapper;
import com.gringotts.technicaltask.web.postcomment.CreatePostCommentData;
import com.gringotts.technicaltask.web.postcomment.PostCommentDto;
import com.gringotts.technicaltask.web.postcomment.UpdatePostCommentData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class PostCommentServiceImpl implements PostCommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final Mapper<PostComment, PostCommentDto> mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PostCommentDto> getAllBy(Long postId, Pageable pageable) {
        final Post post = postRepository.findById(postId)
                                        .orElseThrow(() -> new EntityNotFoundException(Post.class, postId));
        return postCommentRepository.findAllByPost(post, pageable)
                                    .map(mapper::map);
    }

    @Override
    @Transactional
    public void add(CreatePostCommentData data) {
        final Post post = postRepository.findById(data.getPostId())
                                        .orElseThrow(() -> new EntityNotFoundException(Post.class, data.getPostId()));
        final var comment = new PostComment(data.getReview());
        post.addComment(comment);
    }

    @Override
    @Transactional
    public void edit(UpdatePostCommentData data) {
        final PostComment comment = data.getId()
                                        .flatMap(postCommentRepository::findById)
                                        .orElseThrow(() -> new EntityNotFoundException(PostComment.class));
        data.getReview().ifPresent(comment::setReview);
        postCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void remove(Long postId, Long postCommentId) {
        final Post post = postRepository.findById(postId)
                                        .orElseThrow(() -> new EntityNotFoundException(Post.class, postId));
        final PostComment postComment = postCommentRepository.findById(postCommentId)
                                                             .orElseThrow(() -> new EntityNotFoundException(PostComment.class,
                                                                                                            postCommentId));
        post.removeComment(postComment);
    }
}

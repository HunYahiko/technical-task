package com.gringotts.technicaltask.service.postcomment;


import com.gringotts.technicaltask.web.postcomment.CreatePostCommentData;
import com.gringotts.technicaltask.web.postcomment.PostCommentDto;
import com.gringotts.technicaltask.web.postcomment.UpdatePostCommentData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCommentService {
    Page<PostCommentDto> getAllBy(Long postId, Pageable pageable);
    void add(CreatePostCommentData data);
    void edit(UpdatePostCommentData data);
    void remove(Long postId, Long postCommentId);
}

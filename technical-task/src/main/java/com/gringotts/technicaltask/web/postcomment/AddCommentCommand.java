package com.gringotts.technicaltask.web.postcomment;

import lombok.Data;

import java.util.Optional;

@Data
public class AddCommentCommand implements CreatePostCommentData {
    private Long postId;
    private String review;

    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }
}

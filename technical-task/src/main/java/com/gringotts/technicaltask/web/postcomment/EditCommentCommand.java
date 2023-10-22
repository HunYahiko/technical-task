package com.gringotts.technicaltask.web.postcomment;

import lombok.Data;

import java.util.Optional;

@Data
public class EditCommentCommand implements UpdatePostCommentData {
    private Long id;
    private String review;

    @Override
    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public Optional<String> getReview() {
        return Optional.ofNullable(review);
    }
}

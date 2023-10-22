package com.gringotts.technicaltask.web.postcomment;

import java.util.Optional;

public interface CreatePostCommentData {
    Optional<Long> getId();
    Long getPostId();
    String getReview();
}

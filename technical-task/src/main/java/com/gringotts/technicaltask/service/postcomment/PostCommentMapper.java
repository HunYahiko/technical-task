package com.gringotts.technicaltask.service.postcomment;

import com.gringotts.technicaltask.domain.PostComment;
import com.gringotts.technicaltask.utils.Mapper;
import com.gringotts.technicaltask.web.postcomment.PostCommentDto;
import org.springframework.stereotype.Component;

@Component
class PostCommentMapper implements Mapper<PostComment, PostCommentDto> {

    @Override
    public PostCommentDto map(PostComment entity) {
        final var dto = new PostCommentDto();
        dto.setId(entity.getId());
        dto.setReview(entity.getReview());
        return dto;
    }
}

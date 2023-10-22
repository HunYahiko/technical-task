package com.gringotts.technicaltask.service.post;

import com.gringotts.technicaltask.domain.Post;
import com.gringotts.technicaltask.utils.Mapper;
import com.gringotts.technicaltask.web.post.PostDto;
import org.springframework.stereotype.Component;

@Component
class PostMapper implements Mapper<Post, PostDto> {

    @Override
    public PostDto map(Post entity) {
        final var dto = new PostDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        return dto;
    }
}

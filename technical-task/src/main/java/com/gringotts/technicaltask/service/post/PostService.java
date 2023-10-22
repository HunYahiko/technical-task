package com.gringotts.technicaltask.service.post;

import com.gringotts.technicaltask.web.post.CreatePostData;
import com.gringotts.technicaltask.web.post.PostDto;
import com.gringotts.technicaltask.web.post.UpdatePostData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<PostDto> getAll(Pageable pageable);
    PostDto getById(Long id);
    void create(CreatePostData data);
    void update(UpdatePostData data);
    void deleteById(Long id);
}

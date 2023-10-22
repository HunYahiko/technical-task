package com.gringotts.technicaltask.service.post.validator;

import com.gringotts.technicaltask.exception.InvalidPostException;
import com.gringotts.technicaltask.repository.PostRepository;
import com.gringotts.technicaltask.web.post.CreatePostData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UniqueTitleValidator implements AddPostValidator {

    private final PostRepository postRepository;

    @Override
    public void validate(CreatePostData data) {
        if (postRepository.existsByTitle(data.getTitle()))
            throw new InvalidPostException("Post with given title already exists!");
    }
}

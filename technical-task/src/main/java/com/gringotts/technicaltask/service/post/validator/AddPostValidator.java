package com.gringotts.technicaltask.service.post.validator;

import com.gringotts.technicaltask.web.post.CreatePostData;

public interface AddPostValidator {
    void validate(CreatePostData data);
}

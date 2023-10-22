package com.gringotts.technicaltask.web.post;

import java.util.Optional;

public interface UpdatePostData {
    Optional<Long> getId();
    Optional<String> getTitle();
    Optional<String> getDescription();
    Optional<String> getContent();
}

package com.gringotts.technicaltask.web.post;

import lombok.Data;

import java.util.Optional;

@Data
public class UpdatePostCommand implements UpdatePostData {
    private Long id;
    private String title;
    private String description;
    private String content;

    @Override
    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<String> getContent() {
        return Optional.ofNullable(content);
    }
}

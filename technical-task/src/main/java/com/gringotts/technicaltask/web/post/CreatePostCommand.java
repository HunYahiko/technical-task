package com.gringotts.technicaltask.web.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostCommand implements CreatePostData {
    @NotNull(message = "Invalid title: Cannot be null")
    @NotBlank(message = "Invalid title: Cannot be empty")
    @Size(min = 3, max = 150, message = "Invalid title: Must be of 3-150 characters")
    private String title;
    @NotNull(message = "Invalid description: Cannot be null")
    @NotBlank(message = "Invalid description: Cannot be empty")
    @Size(min = 3, max = 150, message = "Invalid description: Must be of 3-150 characters")
    private String description;
    @NotNull(message = "Invalid content: Cannot be null")
    @NotBlank(message = "Invalid content: Cannot be empty")
    @Size(min = 10, max = 3000, message = "Invalid content: Must be of 50-3000 characters")
    private String content;
}

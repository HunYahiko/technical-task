package com.gringotts.technicaltask.web.post;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gringotts.technicaltask.service.post.PostService;
import com.gringotts.technicaltask.service.postcomment.PostCommentService;
import com.gringotts.technicaltask.web.postcomment.AddCommentCommand;
import com.gringotts.technicaltask.web.postcomment.EditCommentCommand;
import com.gringotts.technicaltask.web.postcomment.PostCommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private static final Long POST_ID = 1L;
    private static final Long POST_COMMENT_ID = 1L;

    @Mock
    private PostService postService;
    @Mock
    private PostCommentService postCommentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private Pageable pageable;
    private PostDto postDto;
    private PostCommentDto commentDto;

    @BeforeEach
    void setUp() {
        final var controller = new PostController(postService,
                                                  postCommentService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                                 .build();

        pageable = PageRequest.of(0, 10);

        postDto = new PostDto();
        postDto.setId(POST_ID);
        postDto.setTitle("This is a title");
        postDto.setContent("This is content");

        commentDto = new PostCommentDto();
        commentDto.setId(POST_COMMENT_ID);
        commentDto.setReview("This is a review");

        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                                               .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                                               .withGetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    @Test
    void getPosts_whenInvokedWithoutPageParameters_returnsExpectedResults() throws Exception {
        when(postService.getAll(pageable)).thenReturn(new PageImpl<>(List.of(postDto),
                                                                     pageable,
                                                                     1L));

        mockMvc.perform(get("/posts"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(1)))
               .andExpect(jsonPath("$.content[0].id").value(postDto.getId().intValue()))
               .andExpect(jsonPath("$.content[0].title").value(postDto.getTitle()))
               .andExpect(jsonPath("$.content[0].content").value(postDto.getContent()))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getPosts_whenInvokedWithPageParameters_returnsExpectedResults() throws Exception {
        pageable = PageRequest.of(1, 12);

        when(postService.getAll(pageable)).thenReturn(new PageImpl<>(List.of(postDto),
                                                                     pageable,
                                                                     1L));

        mockMvc.perform(get("/posts").param("page", "1").param("size", "12"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(1)))
               .andExpect(jsonPath("$.content[0].id").value(postDto.getId().intValue()))
               .andExpect(jsonPath("$.content[0].title").value(postDto.getTitle()))
               .andExpect(jsonPath("$.content[0].content").value(postDto.getContent()))
               .andExpect(jsonPath("$.totalPages").value(2))
               .andExpect(jsonPath("$.totalElements").value(13));
    }

    @Test
    void getById_whenInvoked_returnsExpectedResult() throws Exception {
        when(postService.getById(POST_ID)).thenReturn(postDto);

        mockMvc.perform(get("/posts/{id}", POST_ID))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(postDto.getId().intValue()))
               .andExpect(jsonPath("$.title").value(postDto.getTitle()))
               .andExpect(jsonPath("$.content").value(postDto.getContent()));
    }

    @Test
    void create_whenInvoked_createsNewRecord() throws Exception {
        final var data = new CreatePostCommand();
        data.setTitle("This is a title");
        data.setContent("This is content");

        final String json = objectMapper.writeValueAsString(data);

        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isCreated());

        verify(postService).create(data);
    }

    @Test
    void create_whenDataIsNotValid_returnsBadRequest() throws Exception {
        final var data = new CreatePostCommand();
        data.setTitle("");
        data.setContent("This is content");

        final String json = objectMapper.writeValueAsString(data);

        mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isBadRequest());

        verify(postService, never()).create(data);
    }

    @Test
    void update_whenInvoked_updatesRecord() throws Exception {
        final var data = new UpdatePostCommand();
        data.setTitle("This is a new title");
        data.setContent("This is new content");

        final String json = objectMapper.writeValueAsString(data);

        mockMvc.perform(put("/posts/{id}", POST_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isNoContent());

        data.setId(POST_ID);
        verify(postService).update(data);
    }

    @Test
    void delete_whenInvoked_deletesRecord() throws Exception {

        mockMvc.perform(delete("/posts/{id}", POST_ID))
               .andExpect(status().isNoContent());

        verify(postService).deleteById(POST_ID);
    }

    @Test
    void getPostComments_whenInvokedWithoutPageParameters_returnsExpectedResults() throws Exception {
        when(postCommentService.getAllBy(POST_ID, pageable)).thenReturn(new PageImpl<>(List.of(commentDto),
                                                                                       pageable,
                                                                                       1));

        mockMvc.perform(get("/posts/{id}/comments", POST_ID))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(1)))
               .andExpect(jsonPath("$.content[0].id").value(commentDto.getId().intValue()))
               .andExpect(jsonPath("$.content[0].review").value(commentDto.getReview()))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getPostComments_whenInvokedWithPageParameters_returnsExpectedResults() throws Exception {
        pageable = PageRequest.of(1, 12);

        when(postCommentService.getAllBy(POST_ID, pageable)).thenReturn(new PageImpl<>(List.of(commentDto),
                                                                                       pageable,
                                                                                       1));

        mockMvc.perform(get("/posts/{id}/comments", POST_ID).param("page", "1").param("size", "12"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(1)))
               .andExpect(jsonPath("$.content[0].id").value(commentDto.getId().intValue()))
               .andExpect(jsonPath("$.content[0].review").value(commentDto.getReview()))
               .andExpect(jsonPath("$.totalPages").value(2))
               .andExpect(jsonPath("$.totalElements").value(13));
    }

    @Test
    void addComment_whenInvoked_addsRecord() throws Exception {
        final var data = new AddCommentCommand();
        data.setReview("This is a review");

        final String json = objectMapper.writeValueAsString(data);

        mockMvc.perform(post("/posts/{id}/comments", POST_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isCreated());

        data.setPostId(POST_ID);
        verify(postCommentService).add(data);
    }

    @Test
    void editComment_whenInvoked_editsRecord() throws Exception {
        final var data = new EditCommentCommand();
        data.setReview("This is a new review");

        final String json = objectMapper.writeValueAsString(data);

        mockMvc.perform(put("/posts/{postId}/comments/{commentId}", POST_ID, POST_COMMENT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isNoContent());

        data.setId(POST_COMMENT_ID);
        verify(postCommentService).edit(data);
    }

    @Test
    void removeComment_whenInvoked_removesRecord() throws Exception {
        mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", POST_ID, POST_COMMENT_ID))
               .andExpect(status().isNoContent());

        verify(postCommentService).remove(POST_ID, POST_COMMENT_ID);
    }
}
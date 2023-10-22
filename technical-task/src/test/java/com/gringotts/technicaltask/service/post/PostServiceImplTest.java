package com.gringotts.technicaltask.service.post;

import com.gringotts.technicaltask.domain.Post;
import com.gringotts.technicaltask.exception.EntityNotFoundException;
import com.gringotts.technicaltask.exception.InvalidPostException;
import com.gringotts.technicaltask.repository.PostRepository;
import com.gringotts.technicaltask.service.post.validator.AddPostValidator;
import com.gringotts.technicaltask.utils.Mapper;
import com.gringotts.technicaltask.web.post.CreatePostCommand;
import com.gringotts.technicaltask.web.post.PostDto;
import com.gringotts.technicaltask.web.post.UpdatePostCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    private static final long POST_ID = 1L;

    @Mock
    private PostRepository postRepository;
    @Mock
    private Mapper<Post, PostDto> mapper;
    @Mock
    private AddPostValidator validator;

    private PostServiceImpl service;

    private Post post;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        service = new PostServiceImpl(postRepository,
                                      mapper,
                                      List.of(validator));

        post = new Post("This is a title",
                                  "This is content",
                        "This is a description");
        ReflectionTestUtils.setField(post, "id", POST_ID);
        postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());

        lenient().when(mapper.map(post)).thenReturn(postDto);
        lenient().when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
    }

    @Test
    void getAll_whenNoneFound_returnsExpectedResults() {
        final Pageable pageable = PageRequest.of(0, 10);

        when(postRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(),
                                                                         pageable,
                                                                         0));

        final Page<PostDto> returnedPage = service.getAll(pageable);

        assertThat(returnedPage.getContent()).isNotNull()
                                             .isEmpty();
        verify(mapper, never()).map(any(Post.class));
    }

    @Test
    void getAll_whenInvoked_returnsExpectedResults() {
        final Pageable pageable = PageRequest.of(0, 10);

        when(postRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(post),
                                                                         pageable,
                                                                         1));

        final Page<PostDto> returnedPage = service.getAll(pageable);

        assertThat(returnedPage.getContent()).isNotNull()
                                             .isNotEmpty()
                                             .hasSize(1)
                                             .containsExactlyInAnyOrder(postDto);
    }

    @Test
    void getById_whenNotFoundById_throwsEntityNotFoundException() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(POST_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [Post] with id [1] could not be retrieved.");
    }

    @Test
    void getById_whenInvoked_returnsExpectedResult() {
        final var returnedPostDto = service.getById(POST_ID);

        assertThat(returnedPostDto).isEqualTo(postDto);
    }

    @Test
    void create_whenDataIsNotValid_throwsException() {
        final var data = new CreatePostCommand();
        data.setTitle("This is a title");
        data.setContent("This is content");
        final var exception = new InvalidPostException("something is wrong");

        doThrow(exception).when(validator).validate(data);

        assertThatThrownBy(() -> service.create(data))
                .isInstanceOf(InvalidPostException.class)
                .hasMessage(exception.getMessage());
    }

    @Test
    void create_whenInvoked_persistNewPost() {
        final ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        final var data = new CreatePostCommand();
        data.setTitle("This is a title");
        data.setContent("This is content");
        data.setDescription("This is a description");

        service.create(data);

        verify(postRepository).save(captor.capture());
        final Post captured = captor.getValue();
        assertThat(captured.getTitle()).isEqualTo(data.getTitle());
        assertThat(captured.getContent()).isEqualTo(data.getContent());
        assertThat(captured.getDescription()).isEqualTo(data.getDescription());
    }

    @Test
    void update_whenPostNotFound_throwsEntityNotFoundException() {
        final var data = new UpdatePostCommand();
        data.setId(POST_ID);
        data.setTitle("New title");
        data.setContent("New content");

        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(data))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [Post] could not be retrieved.");
    }

    @Test
    void update_whenInvoked_returnsExpectedResults() {
        final ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        final var data = new UpdatePostCommand();
        data.setId(POST_ID);
        data.setTitle("New title");
        data.setContent("New content");
        data.setDescription("New description");

        service.update(data);

        verify(postRepository).save(captor.capture());
        final Post captured = captor.getValue();
        assertThat(captured.getTitle()).isEqualTo("New title");
        assertThat(captured.getContent()).isEqualTo("New content");
        assertThat(captured.getDescription()).isEqualTo("New description");
    }

    @Test
    void deleteById_whenInvoked_deletesEntity() {
        service.deleteById(POST_ID);

        verify(postRepository).deleteById(POST_ID);
    }
}
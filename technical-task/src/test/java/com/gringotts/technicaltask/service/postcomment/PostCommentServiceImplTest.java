package com.gringotts.technicaltask.service.postcomment;

import com.gringotts.technicaltask.domain.Post;
import com.gringotts.technicaltask.domain.PostComment;
import com.gringotts.technicaltask.exception.EntityNotFoundException;
import com.gringotts.technicaltask.repository.PostCommentRepository;
import com.gringotts.technicaltask.repository.PostRepository;
import com.gringotts.technicaltask.utils.Mapper;
import com.gringotts.technicaltask.web.postcomment.AddCommentCommand;
import com.gringotts.technicaltask.web.postcomment.EditCommentCommand;
import com.gringotts.technicaltask.web.postcomment.PostCommentDto;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostCommentServiceImplTest {

    private static final Long POST_ID = 1L;
    private static final Long POST_COMMENT_ID = 1L;

    @Mock
    private PostRepository postRepository;
    @Mock
    private PostCommentRepository postCommentRepository;
    @Mock
    private Mapper<PostComment, PostCommentDto> mapper;

    private PostCommentServiceImpl service;

    private Post post;
    private PostComment comment;

    @BeforeEach
    void setUp() {
        service = new PostCommentServiceImpl(postRepository,
                                             postCommentRepository,
                                             mapper);

        post = new Post("This is a title",
                        "This is content",
                        "This is a description");
        ReflectionTestUtils.setField(post, "id", POST_ID);
        comment = new PostComment("This is a review");
        ReflectionTestUtils.setField(comment, "id", POST_COMMENT_ID);

        lenient().when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        lenient().when(postCommentRepository.findById(POST_COMMENT_ID)).thenReturn(Optional.of(comment));
    }

    @Test
    void getAllBy_whenPostIsNotFound_throwsEntityNotFoundException() {
        final Pageable pageable = PageRequest.of(0, 10);

        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAllBy(POST_ID, pageable))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [Post] with id [1] could not be retrieved.");
    }

    @Test
    void getAllBy_whenInvoked_returnsExpectedResult() {
        final Pageable pageable = PageRequest.of(0, 10);
        final var commentDto = new PostCommentDto();
        commentDto.setId(comment.getId());
        commentDto.setReview(comment.getReview());

        when(postCommentRepository.findAllByPost(post, pageable)).thenReturn(new PageImpl<>(List.of(comment),
                                                                                            pageable,
                                                                                            1));
        when(mapper.map(comment)).thenReturn(commentDto);

        final Page<PostCommentDto> returnedPage = service.getAllBy(POST_ID, pageable);

        assertThat(returnedPage.getContent()).isNotNull()
                                             .isNotEmpty()
                                             .hasSize(1)
                                             .containsExactlyInAnyOrder(commentDto);
    }

    @Test
    void add_whenPostIsNotFound_throwsEntityNotFoundException() {
        final var data = new AddCommentCommand();
        data.setPostId(POST_ID);
        data.setReview("This is a review");

        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.add(data))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [Post] with id [1] could not be retrieved.");
    }

    @Test
    void add_whenInvoked_addsCommentToPost() {
        final var data = new AddCommentCommand();
        data.setPostId(POST_ID);
        data.setReview("This is a review");

        service.add(data);

        assertThat(post.getComments()).isNotNull()
                                      .isNotEmpty()
                                      .hasSize(1);
    }

    @Test
    void edit_whenDataDoesNotContainPostCommentId_throwsEntityNotFoundException() {
        final var data = new EditCommentCommand();
        data.setReview("This is a new review");

        assertThatThrownBy(() -> service.edit(data))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [PostComment] could not be retrieved.");
    }

    @Test
    void edit_whenCommentNotFound_throwsEntityNotFoundException() {
        final var data = new EditCommentCommand();
        data.setId(POST_COMMENT_ID);
        data.setReview("This is a new review");

        when(postCommentRepository.findById(POST_COMMENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.edit(data))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [PostComment] could not be retrieved.");
    }

    @Test
    void edit_whenInvoked_editsComment() {
        final ArgumentCaptor<PostComment> captor = ArgumentCaptor.forClass(PostComment.class);
        final var data = new EditCommentCommand();
        data.setId(POST_COMMENT_ID);
        data.setReview("This is a new review");

        service.edit(data);

        verify(postCommentRepository).save(captor.capture());
        final PostComment captured = captor.getValue();
        assertThat(captured.getId()).isEqualTo(POST_COMMENT_ID);
        assertThat(captured.getReview()).isEqualTo("This is a new review");
    }

    @Test
    void remove_whenPostIsNotFound_throwsEntityNotFoundException() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.remove(POST_ID, POST_COMMENT_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [Post] with id [1] could not be retrieved.");
    }

    @Test
    void remove_whenPostCommentIsNotFound_throwsEntityNotFoundException() {
        when(postCommentRepository.findById(POST_COMMENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.remove(POST_ID, POST_COMMENT_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity of type [PostComment] with id [1] could not be retrieved.");
    }

    @Test
    void remove_whenInvoked_removesComment() {
        post.addComment(comment);

        service.remove(POST_ID, POST_COMMENT_ID);

        assertThat(post.getComments()).isNotNull()
                                      .isEmpty();
    }
}
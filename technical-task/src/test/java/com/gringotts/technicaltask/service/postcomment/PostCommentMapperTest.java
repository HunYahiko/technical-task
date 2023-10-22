package com.gringotts.technicaltask.service.postcomment;

import com.gringotts.technicaltask.domain.PostComment;
import com.gringotts.technicaltask.web.postcomment.PostCommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PostCommentMapperTest {

    private static final long POST_COMMENT_ID = 1L;

    private PostCommentMapper mapper;

    private PostComment postComment;

    @BeforeEach
    void setUp() {
        mapper = new PostCommentMapper();

        postComment = new PostComment("This is a comment");
        ReflectionTestUtils.setField(postComment, "id", POST_COMMENT_ID);
    }

    @Test
    void map_whenInvoked_mapsCorrectly() {
        final PostCommentDto returnedDto = mapper.map(postComment);

        assertThat(returnedDto.getId()).isEqualTo(postComment.getId());
        assertThat(returnedDto.getReview()).isEqualTo(postComment.getReview());
    }

    @Test
    void mapList_whenInvoked_mapsCorrectly() {
        final List<PostCommentDto> returnedList = mapper.mapList(List.of(postComment));

        assertThat(returnedList).isNotNull()
                                .isNotEmpty()
                                .hasSize(1);
        assertThat(returnedList.get(0).getId()).isEqualTo(postComment.getId());
        assertThat(returnedList.get(0).getReview()).isEqualTo(postComment.getReview());
    }
}
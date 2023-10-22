package com.gringotts.technicaltask.service.post;

import com.gringotts.technicaltask.domain.Post;
import com.gringotts.technicaltask.web.post.PostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PostMapperTest {

    private static final long POST_ID = 1L;

    private PostMapper mapper;

    private Post post;

    @BeforeEach
    void setUp() {
        mapper = new PostMapper();
        post = new Post("This is a title",
                        "This is content",
                        "This is a description");
        ReflectionTestUtils.setField(post, "id", POST_ID);
    }

    @Test
    void map_whenInvoked_mapsCorrectly() {
        final PostDto returnedDto = mapper.map(post);

        assertThat(returnedDto.getId()).isEqualTo(post.getId());
        assertThat(returnedDto.getTitle()).isEqualTo(post.getTitle());
        assertThat(returnedDto.getContent()).isEqualTo(post.getContent());
        assertThat(returnedDto.getDescription()).isEqualTo(post.getDescription());
    }

    @Test
    void mapList_whenInvoked_mapsCorrectly() {
        final List<PostDto> returnedList = mapper.mapList(List.of(post));

        assertThat(returnedList).isNotNull()
                                .isNotEmpty()
                                .hasSize(1);
        assertThat(returnedList.get(0).getId()).isEqualTo(post.getId());
        assertThat(returnedList.get(0).getTitle()).isEqualTo(post.getTitle());
        assertThat(returnedList.get(0).getContent()).isEqualTo(post.getContent());
        assertThat(returnedList.get(0).getDescription()).isEqualTo(post.getDescription());
    }
}
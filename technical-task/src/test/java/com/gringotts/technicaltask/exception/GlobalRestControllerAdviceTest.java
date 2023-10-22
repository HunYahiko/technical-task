package com.gringotts.technicaltask.exception;

import com.gringotts.technicaltask.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalRestControllerAdviceTest {

    private GlobalRestControllerAdvice advice;

    @BeforeEach
    void setUp() {
        advice = new GlobalRestControllerAdvice();
    }

    @Test
    void handleEntityNotFoundException_whenInvoked_returnsExpectedResult() {
        final var exception = new EntityNotFoundException(Post.class, 1L);

        final ResponseEntity<Map<String, List<String>>> returnedResponse = advice.handle(exception);

        assertThat(returnedResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        assertThat(returnedResponse.getBody())
                .containsEntry("errors", List.of("Entity of type [Post] with id [1] could not be retrieved."));
    }
}
package com.gringotts.technicaltask.repository;

import com.gringotts.technicaltask.TechnicalPostgreSQLContainer;
import com.gringotts.technicaltask.domain.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {PostRepositoryTest.Initializer.class})
@Testcontainers
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @Container
    private final static PostgreSQLContainer postgreSQLContainer = TechnicalPostgreSQLContainer.getInstance();

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }

    @BeforeEach
    void setUp() {
        post = new Post("This is a title",
                        "This is content",
                        "This is a description");
        postRepository.save(post);
    }

    @Test
    void existsByTitle_whenNotFoundByTitle_returnsFalse() {
        final boolean returnedValue = postRepository.existsByTitle("bogus title");

        assertThat(returnedValue).isFalse();
    }

    @Test
    void existsByTitle_whenFoundByTitle_returnsTrue() {
        final boolean returnedValue = postRepository.existsByTitle(post.getTitle());

        assertThat(returnedValue).isTrue();
    }

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
    }
}
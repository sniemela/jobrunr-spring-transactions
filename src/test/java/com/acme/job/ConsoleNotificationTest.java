package com.acme.job;

import com.acme.service.CommentService;
import com.acme.service.PostService;
import org.jobrunr.jobs.states.StateName;
import org.jobrunr.storage.StorageProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConsoleNotificationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private StorageProvider storageProvider;

    @AfterEach
    void cleanup() {
        for (StateName stateName : StateName.values()) {
            storageProvider.deleteJobsPermanently(stateName, Instant.now().plusSeconds(10));
        }
    }

    @Test
    void shouldSaveAJobWhenPostIsCreated() {
        // Given
        assertThat(storageProvider.getJobStats().getTotal()).isZero();

        // When
        postService.createPost("Test post");

        // Then
        assertThat(storageProvider.getJobStats().getTotal()).isOne();
    }

    @Test
    void shouldSaveAJobWhenPostIsUpdated() {
        // Given
        assertThat(storageProvider.getJobStats().getTotal()).isZero();

        // When
        var post = postService.createPost("Test post");

        // Then
        assertThat(storageProvider.getJobStats().getTotal()).isOne();

        // When
        commentService.postComment(post.getId(), "Test comment");

        // Then
        assertThat(storageProvider.getJobStats().getTotal()).isEqualTo(2);
    }
}

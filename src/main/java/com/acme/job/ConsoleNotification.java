package com.acme.job;

import com.acme.domain.event.PostCreated;
import com.acme.domain.event.PostUpdated;
import com.acme.exception.PostNotFoundException;
import com.acme.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsoleNotification {

    private final PostRepository postRepository;
    private final JobScheduler jobScheduler;

    @TransactionalEventListener
    public void onPostCreated(PostCreated event) {
        jobScheduler.enqueue(UUID.randomUUID(), () -> logPostCreated(event.id()));
    }

    public void logPostCreated(long postId) {
        var post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        log.info("Post created: {}, {}", post.getId(), post.getContent());
    }

    @TransactionalEventListener
    public void onPostUpdated(PostUpdated event) {
        jobScheduler.enqueue(UUID.randomUUID(), () -> logPostUpdated(event.id()));
    }

    public void logPostUpdated(long id) {
        var post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        log.info("Post updated: {}, {}", post.getId(), post.getContent());
    }
}

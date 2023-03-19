package com.acme.config.jobrunr;

import com.acme.domain.Post;
import com.acme.repository.PostRepository;
import com.acme.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.JobNotFoundException;
import org.jobrunr.storage.StorageProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
public class JobRunrTest {

    static final long POST_ID = IdGenerator.randomId();
    static final UUID JOB_ID = UUID.randomUUID();

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private StorageProvider storageProvider;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        storageProvider.deletePermanently(JOB_ID);
    }

    @Test
    void shouldParticipateInSpringManagedTransaction() {
        var dummyService = beanFactory.createBean(DummyService.class);

        // When
        assertThatThrownBy(dummyService::save).isInstanceOf(ProjectAndJobRollbackException.class);

        // Then
        assertThat(postRepository.findById(POST_ID)).isEmpty();
        assertThatThrownBy(() -> storageProvider.getJobById(JOB_ID)).isInstanceOf(JobNotFoundException.class);
    }

    @RequiredArgsConstructor
    static class DummyService {

        private final PostRepository postRepository;
        private final JobScheduler jobScheduler;

        @Transactional
        public void save() throws InterruptedException {
            postRepository.save(Post.create());

            jobScheduler.enqueue(JOB_ID, () -> System.out.println("Should not happen"));

            Thread.sleep(1000);

            throw new ProjectAndJobRollbackException("Project and job should be rolled back!");
        }
    }

    static class ProjectAndJobRollbackException extends RuntimeException {

        public ProjectAndJobRollbackException(String s) {
            super(s);
        }
    }
}

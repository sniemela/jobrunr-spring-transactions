package com.acme.service;

import com.acme.domain.Post;
import com.acme.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(String content) {
        var post = Post.create();
        post.setContent(content);

        return postRepository.save(post);
    }
}

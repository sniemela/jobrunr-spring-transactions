package com.acme.service;

import com.acme.domain.Comment;
import com.acme.exception.PostNotFoundException;
import com.acme.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;

    @Transactional
    public Comment postComment(long postId, String commentContent) {
        var post = postRepository.findByIdWithComments(postId).orElseThrow(PostNotFoundException::new);
        var comment = Comment.create(commentContent);

        post.addComment(comment);
        postRepository.save(post);

        return comment;
    }
}

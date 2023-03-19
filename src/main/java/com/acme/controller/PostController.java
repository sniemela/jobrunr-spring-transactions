package com.acme.controller;

import com.acme.domain.Comment;
import com.acme.domain.Post;
import com.acme.exception.PostNotFoundException;
import com.acme.repository.PostRepository;
import com.acme.service.CommentService;
import com.acme.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final CommentService commentService;

    @PostMapping
    public Post createNewPost(@RequestBody CreateNewPostRequest postRequest) {
        return postService.createPost(postRequest.content());
    }

    @GetMapping("/{postId}")
    public Post getPostById(@PathVariable("postId") long postId) {
        return postRepository.findByIdWithComments(postId).orElseThrow(PostNotFoundException::new);
    }

    @PostMapping("/{postId}/comments")
    public Comment postNewComment(@PathVariable("postId") long postId, @RequestBody CreateNewCommentRequest commentRequest) {
        return commentService.postComment(postId, commentRequest.content());
    }
}

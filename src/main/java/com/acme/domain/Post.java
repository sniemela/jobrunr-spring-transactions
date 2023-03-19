package com.acme.domain;

import com.acme.domain.event.PostCreated;
import com.acme.domain.event.PostUpdated;
import com.acme.util.IdGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Post extends AbstractAggregateRoot<Post> {

    @Id
    private Long id;
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Version
    private Long version;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public static Post create() {
        var post = new Post();
        post.id = IdGenerator.randomId();
        post.version = null;
        post.registerEvent(new PostCreated(post.id));
        return post;
    }

    public void setContent(String newContent) {
        if (content != null && !content.equals(newContent)) {
            registerEvent(new PostUpdated(id));
        }
        content = newContent;
    }

    public void addComment(Comment comment) {
        comment.setPost(this);
        comments.add(comment);
        registerEvent(new PostUpdated(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(content, post.content) && Objects.equals(version, post.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, version);
    }
}

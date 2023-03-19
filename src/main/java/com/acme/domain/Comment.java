package com.acme.domain;

import com.acme.util.IdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Comment {

    @Id
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    @Version
    private Long version;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    void setPost(Post post) {
        this.post = post;
    }

    public static Comment create(String content) {
        var c = new Comment();
        c.id = IdGenerator.randomId();
        c.content = content;
        c.version = null;
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(content, comment.content) && Objects.equals(version, comment.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, version);
    }
}

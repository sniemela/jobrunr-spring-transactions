package com.acme.repository;

import com.acme.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p left join fetch p.comments where p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") long id);
}

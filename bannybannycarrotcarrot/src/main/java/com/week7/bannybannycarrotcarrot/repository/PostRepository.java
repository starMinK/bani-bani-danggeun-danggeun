
package com.week7.bannybannycarrotcarrot.repository;

import com.week7.bannybannycarrotcarrot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByOrderByIdDesc();
}

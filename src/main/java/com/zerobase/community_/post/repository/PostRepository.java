package com.zerobase.community_.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.zerobase.community_.post.entity.Post;

@Transactional
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}

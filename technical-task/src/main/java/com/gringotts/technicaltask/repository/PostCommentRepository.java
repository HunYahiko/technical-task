package com.gringotts.technicaltask.repository;

import com.gringotts.technicaltask.domain.Post;
import com.gringotts.technicaltask.domain.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Page<PostComment> findAllByPost(Post post, Pageable pageable);
}

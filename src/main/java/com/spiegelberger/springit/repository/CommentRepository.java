package com.spiegelberger.springit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiegelberger.springit.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

}

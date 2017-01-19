package com.capstone08.springstudy.data;

import com.capstone08.springstudy.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByOrderByIdDesc();

    Post findById(int id);
}
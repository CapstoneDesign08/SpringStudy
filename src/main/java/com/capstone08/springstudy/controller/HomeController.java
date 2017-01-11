package com.capstone08.springstudy.controller;

import com.capstone08.springstudy.data.PostRepository;
import com.capstone08.springstudy.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostRepository postRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String board(Model model) {
        List<Post> postList = postRepository.findAllByOrderByIdDesc();
        model.addAttribute("posts", postList);

        return "Home";
    }

    @RequestMapping("/write")
    public String moveToWrite(){

        return "Write";
    }

    @RequestMapping("/postview/{id}")
    public String moveToPostView(@PathVariable int id, ModelMap modelMap){
        Post post = postRepository.findById(id);
        post.setHit();
        postRepository.save(post);
        modelMap.put("post", post);

        return "PostView";
    }
}
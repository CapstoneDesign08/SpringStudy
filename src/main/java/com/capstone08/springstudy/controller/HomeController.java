package com.capstone08.springstudy.controller;

import com.capstone08.springstudy.data.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Autowired
    private PostRepository postRepository;


    // Home화면에서 등록된 모든 게시물들의 리스트들을 id의 '내림차순'으로 잘 보여줄수 있도록 채우시오.
    // model전송시 반드시 "posts"라는 키 사용
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String board() {

        return "Home";
    }

    // 주소 "/write"로 가면 "Write"페이지로 이동하게 만드시오.
    @RequestMapping("/write")
    public String moveToWrite(){

        return "";
    }

    // 등록된 post객체의 id를 이용하여 "/postview/{id}"로 가면 "Postview"페이지로 이동하게 만드시오.
    // ModelMap전송시 반드시 "post"라는 키 사용
    @RequestMapping("/postview/{id}")
    public String moveToPostView( ){

        return "";
    }
}
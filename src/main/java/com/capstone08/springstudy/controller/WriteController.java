package com.capstone08.springstudy.controller;

import com.capstone08.springstudy.data.PostRepository;
import com.capstone08.springstudy.exception.FoundBlankException;
import com.capstone08.springstudy.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WriteController {

    @Autowired
    private PostRepository postRepository;

    // "Write"페이지로 이동하게되면 nick, subject, content를 작성하도록 한다
    // post 모델의 setDate를 이용하여 오늘날짜를 설정하시오. "yyyy/MM/dd"
    // nick(닉네임)과 subject(제목)에 빈칸이 오지않도록 합니다. 빈칸이 오면 FoundBlankException을 throw하도록
    // 작성버튼을 누르면 생성된 객체의 "/postview/{id}" 주소로 이동하도록하시오.
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String writePost(Post post) throws FoundBlankException {



        return "";
    }

    // FoundBlankException발생시 "ErrorPage"를 띄우시오.
    @ExceptionHandler(FoundBlankException.class)
    public String foundException() {
        return "";
    }
}
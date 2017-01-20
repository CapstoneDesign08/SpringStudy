package com.capstone08.springstudy.controller;

import com.capstone08.springstudy.data.PostRepository;
import com.capstone08.springstudy.exception.FoundBlankException;
import com.capstone08.springstudy.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ModifyController {

    @Autowired
    private PostRepository postRepository;
    // "Modify"페이지로 오면 nick, subject, content를 수정한 내용을 입력한걸 저장하도록 하시오.
    // nick(닉네임)과 subject(제목)에 빈칸이 오지않도록 합니다. 빈칸이 오면 FoundBlankException을 throw하도록 하시오.
    // 작성버튼을 누르면 생성된 객체의 "/postview/{id}" 주소로 이동하도록하시오.
    @RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
    public String modifyPost(@PathVariable int id, Post post) throws FoundBlankException {


        return "";
    }

    // FoundBlankException발생시 "ErrorPage"를 띄우시오.
    @ExceptionHandler(FoundBlankException.class)
    public String foundException(){
        return "";
    }
}

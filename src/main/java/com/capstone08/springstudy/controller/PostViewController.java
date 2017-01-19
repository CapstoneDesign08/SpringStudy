package com.capstone08.springstudy.controller;

import com.capstone08.springstudy.data.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostViewController {

    @Autowired
    private PostRepository postRepository;

    // "PostView"페이지에서 삭제버튼을 클릭하면 "/postview/del/{id}"로 이동하는데
    // id에 해당하는 객체가 삭제되도록 만드시오. 그리고 삭제가 완료되면 "Home"페이지로 이동하도록 하시오.
    @RequestMapping("/postview/del/{id}")
    public String deletePost() {



        return "";
    }

    // "PostView"페이지에서 수정버튼을 클릭하면 "/postview/modify/{id}"로 이동하는데
    // id에 해당하는 객체의 "Modify"페이지로 이동하도록 만드시오.
    @RequestMapping(value = "/postview/modify/{id}", method = RequestMethod.GET)
    public String moveToModify () {

        return "";
    }
}
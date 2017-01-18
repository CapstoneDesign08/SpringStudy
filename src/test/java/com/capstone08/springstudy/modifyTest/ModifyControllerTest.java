package com.capstone08.springstudy.modifyTest;

import com.capstone08.springstudy.controller.ModifyController;
import com.capstone08.springstudy.data.PostRepository;
import com.capstone08.springstudy.model.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class ModifyControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private ModifyController modifyController;

    @Mock
    private PostRepository postRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(modifyController)
                .build();
    }

    @Test       // 제목과 닉네임에 공백이 들어간채로 등록하면 Error페이지로 제대로 이동하는가
    public void modifyExceptionTest() throws Exception {
        doAnswer(invocation -> {
            Post post = (Post) invocation.getArguments()[0];
            post.setId(1);
            return null;
        }).when(postRepository).save(any(Post.class));

        mockMvc.perform(
                post("/modify/1")
                        .param("subject", "")
        ).andExpect(view().name("ErrorPage"));

        mockMvc.perform(
                post("/modify/1")
                        .param("nick", "")
        ).andExpect(view().name("ErrorPage"));

    }

    @Test       // 값을 수정하면 값이 제대로 해당 post객체의 postview페이지로 이동하는가
    public void modifyPostTest() throws Exception {
        doAnswer(invocation -> {
            Post post = (Post) invocation.getArguments()[0];
            post.setId(1);
            return null;
        }).when(postRepository).save(any(Post.class));

        mockMvc.perform(
                post("/modify/1")
                        .param("nick", "NICK")
                        .param("subject", "SUBJECT")
                        .param("content", "CONTENT")
        ).andExpect(redirectedUrl("/postview/1"));
        verify(postRepository).save(any(Post.class));
    }

}

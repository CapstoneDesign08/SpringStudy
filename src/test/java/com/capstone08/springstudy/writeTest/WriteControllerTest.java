package com.capstone08.springstudy.writeTest;

import com.capstone08.springstudy.controller.WriteController;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class WriteControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private WriteController writeController;

    @Mock
    private PostRepository postRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(writeController)
                .build();
    }

    @Test       // 값을 모두 입력하면 해당객체의 postview페이지로 넘어가는가
    public void writePostTest() throws Exception {
        doAnswer(invocation -> {
            Post post = (Post) invocation.getArguments()[0];
            post.setId(2);
            return null;
        }).when(postRepository).save(any(Post.class));

        mockMvc.perform(
                post("/write")
                        .param("nick", "NICK")
                        .param("subject", "SUBJECT")
                        .param("content", "CONTENT")
        ).andExpect(redirectedUrl("/postview/2"));
        verify(postRepository).save(any(Post.class));

    }

    @Test    // 제목과 닉네임에 공백이 들어간채로 등록하면 Error 페이지로 제대로 이동하는가
    public void writeExceptionTest() throws Exception {
        //   Mockito.when(postRepository.findById(2)).thenThrow(FOUNDBLANKException.class);

        mockMvc.perform(
                post("/write")
                        .param("subject", "")
        ).andExpect(view().name("ErrorPage"));

        mockMvc.perform(
                post("/write")
                        .param("nick", "")
        ).andExpect(view().name("ErrorPage"));

    }

}
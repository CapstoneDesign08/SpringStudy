package com.capstone08.springstudy.postviewTest;

import com.capstone08.springstudy.controller.PostViewController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PostViewControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private PostViewController postViewController;

    @Mock
    private PostRepository postRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(postViewController)
                .build();
    }

    @Test       // 삭제 클릭시 post객체 데이터가 제대로 삭제가 되는가
    public void deletePostTest() throws Exception {
        doAnswer(invocation -> {
            Post post = (Post) invocation.getArguments()[0];
            post.setId(2);
            return null;
        }).when(postRepository).save(any(Post.class));

        mockMvc.perform(
                get("/postview/del/2")
        ).andExpect(redirectedUrl("/"));
        verify(postRepository, times(1)).delete(2);

    }

    @Test       // modify페이지로 제대로 넘어가는가
    public void moveToModifyTest() throws Exception {
     //   Post post= new Post.PostBuilder(2).withNick("NICK").withSubject("SUBJECT").withContent("CONTENT").withDate("2017/01/08").withHit(10).build();
    //    when(postRepository.findById(2)).thenReturn(post);

        mockMvc.perform(get("/postview/modify/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("Modify"));
    }

}
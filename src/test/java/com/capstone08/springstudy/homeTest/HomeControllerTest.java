package com.capstone08.springstudy.homeTest;

import com.capstone08.springstudy.controller.HomeController;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private HomeController homeController;

    @Mock
    private PostRepository postRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(homeController)
                .build();
    }

    @Test       // post객체 리스트가 제대로 보여주는가(id내림차순으로)
    public void boardTest() throws Exception {

            List<Post> posts = Arrays.asList(
                    new Post.PostBuilder(1).withNick("NICK").withSubject("SUBJECT").withContent("CONTENT").withDate("2017/01/08").withHit(1).build(),
                    new Post.PostBuilder(2).withNick("HONG").withSubject("CAT").withContent("CONTENT").withDate("2017/01/08").withHit(1).build(),
                    new Post.PostBuilder(3).withNick("CHAN").withSubject("DOG").withContent("CONTENT").withDate("2017/01/08").withHit(1).build(),
                    new Post.PostBuilder(4).withNick("CHA").withSubject("AOA").withContent("CONTENT").withDate("2017/01/08").withHit(1).build()
            );
            when(postRepository.findAllByOrderByIdDesc()).thenReturn(posts);

            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("Home"))
                    .andExpect(model().attribute("posts", posts));
            verify(postRepository).findAllByOrderByIdDesc();

    }

    @Test   // Write페이지로 이동할수 있는가
    public void moveToWriteTest() throws Exception {
        mockMvc.perform(get("/write"))
                .andExpect(view().name("Write"));
    }

    @Test   // PostView페이지로 이동할 수 있는가
    public void moveToPostViewTest() throws Exception {
        Post post= new Post.PostBuilder(2).withNick("NICK").withSubject("SUBJECT").withContent("CONTENT").withDate("2017/01/08").withHit(10).build();
        when(postRepository.findById(2)).thenReturn(post);

        mockMvc.perform(get("/postview/2"))
                .andExpect(view().name("PostView"))
                .andExpect(model().attribute("post", post));
        verify(postRepository).findById(2);

    }

}
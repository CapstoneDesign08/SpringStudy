package com.capstone08.springstudy.postviewTest;

import com.capstone08.springstudy.AppConfig;
import com.capstone08.springstudy.controller.HomeController;
import com.capstone08.springstudy.data.PostRepository;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebIntegrationTest(randomPort = true)
public class PostViewSeleniumTest {

    @Value("${local.server.port}")
    private int port;

    private MockMvc mockMvc;

    @InjectMocks
    private HomeController homeController;

    @Mock
    private PostRepository postRepository;

    static WebDriver driver;

    static Properties pro;
    static String connectionURL;
    static String username;
    static String password;

    static Connection conn;
    static Statement stmt;

    @Before
    public void setUp() throws Exception {
        File path = new File("");
        System.out.println(path.getAbsolutePath());

        pro = new Properties();
        pro.load(new FileInputStream(path.getAbsolutePath() + "/src/main/resources/application.properties"));
        connectionURL = pro.getProperty("spring.datasource.url");
        username = pro.getProperty("spring.datasource.username");
        password = pro.getProperty("spring.datasource.password");

        conn = DriverManager.getConnection(connectionURL, username, password);
        stmt = conn.createStatement();

        mockMvc = MockMvcBuilders
                .standaloneSetup(homeController)
                .build();

        if (SystemUtils.IS_OS_WINDOWS)
            System.setProperty("webdriver.chrome.driver", path.getAbsolutePath() + "/src/test/resources/chromedriver.exe");
        else if(SystemUtils.IS_OS_LINUX)
            System.setProperty("webdriver.chrome.driver", path.getAbsolutePath() + "/src/test/resources/chromedriver");

        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test       // post객체 하나 클릭시 입력한 값이 제대로 들어가는가
    public void postViewTest() throws Exception {

        String query = "DELETE FROM post;" ;
        stmt.executeUpdate(query);

        query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);" ;
        stmt.executeUpdate(query);


        String baseURL = "http://localhost:" + port;
        driver.get(baseURL);

        driver.findElement(By.className("bbsSubject")).click();

        WebElement td = driver.findElement(By.cssSelector("td.id_nick"));
        assertEquals( "2 | 작성자 : TEST", td.getText());

        td = driver.findElement(By.cssSelector("td.subject"));
        assertEquals("제목 : TESTSUBJECT", td.getText());

        td = driver.findElement(By.cssSelector("td.content"));
        assertEquals("TESTCONTENT", td.getText());
    }

    @Test    // postview로 들어오면 조회수가 제대로 적용되는가
    public void postViewHitTest() throws Exception {

        // Class.forName("com.mysql.jdbc.Driver").newInstance();
        try {
            String query = "DELETE FROM post;";
            stmt.executeUpdate(query);
            query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);";
            stmt.executeUpdate(query);

            String baseURL = "http://localhost:" + port;
            driver.get(baseURL);

            WebElement td = driver.findElement(By.cssSelector("td.bbsNo"));
            int expected_hit = Integer.parseInt(td.getText()) + 1;
            driver.findElement(By.className("bbsSubject")).click();


            td = driver.findElement(By.cssSelector("td.hit"));
            assertEquals("조회수 " + Integer.toString(expected_hit), td.getText());

            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);

        } catch(AssertionError e){
            Assert.fail("실패");
            String query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);

        }
    }

    @Test       // 뒤로가기 버튼이 제대로 작동하는가
    public void postViewBackTest() throws Exception {
        try {
            String query = "DELETE FROM post;";
            stmt.executeUpdate(query);

            query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);";
            stmt.executeUpdate(query);

            String baseURL = "http://localhost:" + port + "/postview/2";
            driver.get(baseURL);

            driver.findElement(By.className("back")).click();

            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("Home"));

            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);

        } catch (AssertionError e){
            Assert.fail("실패");

            String query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);
        }
    }

    @After
    public void tearDown() {
        this.driver.quit();
    }
}
package com.capstone08.springstudy.postviewTest;

import com.capstone08.springstudy.AppConfig;
import com.capstone08.springstudy.controller.HomeController;
import com.capstone08.springstudy.data.PostRepository;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
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

        Capabilities caps = new DesiredCapabilities();
        ((DesiredCapabilities) caps).setJavascriptEnabled(true);
        ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);
        if (SystemUtils.IS_OS_WINDOWS) {
            ((DesiredCapabilities) caps).setCapability(
                    PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                    path.getAbsolutePath() + "/src/test/resources/phantomjs-2.1.1-windows/bin/phantomjs.exe"
            );
        } else if(SystemUtils.IS_OS_LINUX) {
            ((DesiredCapabilities) caps).setCapability(
                    PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                    path.getAbsolutePath() + "/src/test/resources/phantomjs-2.1.1-linux-x86_64/bin/phantomjs"
            );
        }

        driver = new PhantomJSDriver(caps);

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test       // post객체 하나 클릭시 입력한 값이 제대로 들어가는가
    public void postViewTest() throws Exception {
        String query;
        try {
            query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);" ;
            stmt.executeUpdate(query);

            String baseURL = "http://localhost:" + port + "/postview/2";
            driver.get(baseURL);

            WebElement td = driver.findElement(By.cssSelector("td.id"));
            assertEquals( "2", td.getText());

            td = driver.findElement(By.cssSelector("td.nick"));
            assertEquals( "TEST", td.getText());

            td = driver.findElement(By.cssSelector("td.subject"));
            assertEquals("TESTSUBJECT", td.getText());

            td = driver.findElement(By.cssSelector("td.content"));
            assertEquals("TESTCONTENT", td.getText());
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);
        }
    }

    @Test    // postview로 들어오면 조회수가 제대로 적용되는가
    public void postViewHitTest() throws Exception{
        String query;
        try {
            query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);";
            stmt.executeUpdate(query);

            String baseURL = "http://localhost:" + port;
            driver.get(baseURL);

            WebElement td = driver.findElement(By.cssSelector("td.bbsNo"));
            int expected_hit = Integer.parseInt(td.getText()) + 1;
            //driver.findElement(By.className("bbsSubject")).click();

            baseURL = "http://localhost:" + port + "/postview/2";
            driver.get(baseURL);

            td = driver.findElement(By.cssSelector("td.hit"));
            assertEquals(Integer.toString(expected_hit), td.getText());
        }
        catch(Exception e) {
            throw e;
        }
        finally {
            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);
        }
    }

    @Test       // 뒤로가기 버튼이 제대로 작동하는가
    public void postViewBackTest() throws Exception {
        String query;
        try {
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
        }
        catch (Exception e) {

        }
        finally {
            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);
        }
    }

    @After
    public void tearDown() {
        this.driver.quit();
    }
}

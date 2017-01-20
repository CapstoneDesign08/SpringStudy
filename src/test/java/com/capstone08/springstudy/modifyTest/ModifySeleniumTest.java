package com.capstone08.springstudy.modifyTest;

import com.capstone08.springstudy.AppConfig;
import com.capstone08.springstudy.controller.ModifyController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebIntegrationTest(randomPort = true)
public class ModifySeleniumTest {

    @Value("${local.server.port}")
    private int port;

    private MockMvc mockMvc;

    @InjectMocks
    private ModifyController modifyController;

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
                .standaloneSetup(modifyController)
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

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test       // modify 페이지로 왔을때 default값이 제대로 들어가있는가 (그전에 있던값)
    public void modifyDefaultTest() throws Exception {
        String query;
        try {
            query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);" ;
            stmt.executeUpdate(query);

            String baseURL = "http://localhost:" + port + "/postview/modify/2";
            driver.get(baseURL);

            WebElement td = driver.findElement(By.name("nick"));
            assertEquals("TEST", td.getAttribute("value"));
            td = driver.findElement(By.name("subject"));
            assertEquals("TESTSUBJECT", td.getAttribute("value"));
            td = driver.findElement(By.name("content"));
            assertEquals("TESTCONTENT", td.getAttribute("value"));
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);
        }
    }

    @Test       // 수정한 값대로 제대로 postview에 보여지는가
    public void modifyPostViewTest() throws Exception {
        String query;
        try {
            query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);" ;
            stmt.executeUpdate(query);

            String baseURL = "http://localhost:" + port + "/postview/modify/2";
            driver.get(baseURL);

            driver.findElement(By.name("nick")).clear();
            driver.findElement(By.name("subject")).clear();
            driver.findElement(By.name("content")).clear();

            driver.findElement(By.name("nick")).sendKeys("MODIFY_NICK");
            driver.findElement(By.name("subject")).sendKeys("MODIFY_SUBJECT");
            driver.findElement(By.name("content")).sendKeys("MODIFY_CONTENT");
            driver.findElement(By.className("com_modify")).click();

            WebElement td = driver.findElement(By.cssSelector("td.id"));
            assertEquals( "2", td.getText());

            td = driver.findElement(By.cssSelector("td.nick"));
            assertEquals( "MODIFY_NICK", td.getText());

            td = driver.findElement(By.cssSelector("td.subject"));
            assertEquals("MODIFY_SUBJECT", td.getText());

            td = driver.findElement(By.cssSelector("td.content"));
            assertEquals("MODIFY_CONTENT", td.getText());
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);
        }

    }

    @Test       // 뒤로가기버튼이 제대로 작동하는가
    public void modifyBackTest() throws Exception {
        String query;
        try {
            query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);" ;
            stmt.executeUpdate(query);

            String baseURL = "http://localhost:" + port + "/postview/modify/2";
            driver.get(baseURL);

            driver.findElement(By.className("back")).click();

            mockMvc.perform(post("/modify/2"))
                    .andExpect(redirectedUrl("/postview/2"));
        }
        catch (Exception e) {
            throw e;
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

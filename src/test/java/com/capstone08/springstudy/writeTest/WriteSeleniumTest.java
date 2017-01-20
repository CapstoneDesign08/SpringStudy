package com.capstone08.springstudy.writeTest;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebIntegrationTest(randomPort = true)
public class WriteSeleniumTest {

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

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test   // postview 페이지로 넘어갔을때 입력한 값이 제대로 들어갔는가
    public void writePostViewTest() throws Exception {
        String query;
        try {
            String baseURL = "http://localhost:" + port + "/write";
            driver.get(baseURL);

            driver.findElement(By.name("nick")).sendKeys("NICK");
            driver.findElement(By.name("subject")).sendKeys("SUBJECT");
            driver.findElement(By.name("content")).sendKeys("CONTENT");
            driver.findElement(By.tagName("form")).submit();

            WebElement td = driver.findElement(By.cssSelector("td.id"));
            assertEquals( "1", td.getText());

            td = driver.findElement(By.cssSelector("td.nick"));
            assertEquals( "NICK", td.getText());

            td = driver.findElement(By.cssSelector("td.hit"));
            assertEquals("1", td.getText());

            td = driver.findElement(By.cssSelector("td.subject"));
            assertEquals("SUBJECT", td.getText());

            td = driver.findElement(By.cssSelector("td.content"));
            assertEquals("CONTENT", td.getText());
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            query = "TRUNCATE TABLE post;" ;
            stmt.executeUpdate(query);
        }
    }

    @Test       // 작성중 뒤로가기 버튼이 제대로 작동하는가
    public void writeBackTest() throws Exception {
        String baseURL = "http://localhost:" + port + "/write";
        driver.get(baseURL);

        driver.findElement(By.className("back")).click();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"));
    }

    @Test       // 객체가 생성되면 Home에 제대로 반영되는가
    public void writeHomeTest() throws Exception {
        String query;
        try {
            String baseURL = "http://localhost:" + port + "/write";
            driver.get(baseURL);

            driver.findElement(By.name("nick")).sendKeys("NICK");
            driver.findElement(By.name("subject")).sendKeys("SUBJECT");
            driver.findElement(By.name("content")).sendKeys("CONTENT");
            driver.findElement(By.tagName("form")).submit();

            driver.findElement(By.className("back")).click();

            List<WebElement> div = driver.findElements(By.cssSelector("div.ps"));
            assertEquals(1, div.size());
            WebElement td = driver.findElement(By.cssSelector("td.bbsNo"));
            assertEquals("1", td.getText());
            td = driver.findElement(By.cssSelector("td.bbsSubject"));
            assertEquals("SUBJECT", td.getText());
            td = driver.findElement(By.cssSelector("td.bbsNick"));
            assertEquals("NICK", td.getText());
            Date d = new Date();
            SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd");
            td = driver.findElement(By.cssSelector("td.date"));
            assertEquals(today.format(d), td.getText());
            td = driver.findElement(By.cssSelector("td.hit"));
            assertEquals("1", td.getText());
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            query = "TRUNCATE TABLE post;";
            stmt.executeUpdate(query);
        }
    }

    @Test   //write에서 post객체를 생성하면 date가 오늘날짜로 설정되는가
    public void writeDateTest() throws Exception {
        String query;
        try {

            String baseURL = "http://localhost:" + port + "/write";
            driver.get(baseURL);

            driver.findElement(By.name("nick")).sendKeys("NICK");
            driver.findElement(By.name("subject")).sendKeys("SUBJECT");
            driver.findElement(By.name("content")).sendKeys("CONTENT");
            driver.findElement(By.tagName("form")).submit();

            baseURL = "http://localhost:" + port;
            driver.get(baseURL);

            Date d = new Date();
            SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd");

            WebElement td = driver.findElement(By.className("date"));
            assertEquals(today.format(d), td.getText());
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

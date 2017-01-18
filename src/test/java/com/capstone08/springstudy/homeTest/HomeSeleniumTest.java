package com.capstone08.springstudy.homeTest;

import com.capstone08.springstudy.AppConfig;
import com.capstone08.springstudy.controller.PostViewController;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebIntegrationTest(randomPort = true)
public class HomeSeleniumTest {

    @Value("${local.server.port}")
    private int port;

    @InjectMocks
    private PostViewController postViewController;

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

        if (SystemUtils.IS_OS_WINDOWS)
            System.setProperty("webdriver.chrome.driver", path.getAbsolutePath() + "/src/test/resources/chromedriver.exe");
        else if(SystemUtils.IS_OS_LINUX)
            System.setProperty("webdriver.chrome.driver", path.getAbsolutePath() + "/src/test/resources/chromedriver");

        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test       // post의 객체의 id, 제목, 닉네임, 해당조회수가 적용되었나
    public void HomePostTest() throws Exception {
        String query = "DELETE FROM post;" ;
        stmt.executeUpdate(query);

        query = "Insert Into post(id, nick ,subject, content, date, hit) VALUES (2, 'TEST', 'TESTSUBJECT', 'TESTCONTENT', '2017/01/16', 2);" ;
        stmt.executeUpdate(query);

        String baseURL = "http://localhost:" + port;
        driver.get(baseURL);

        List<WebElement> div = driver.findElements(By.cssSelector("div.ps"));
        assertEquals(1, div.size());
        WebElement td = driver.findElement(By.cssSelector("td.bbsNo"));
        assertEquals("2", td.getText());
        td = driver.findElement(By.cssSelector("td.bbsSubject"));
        assertEquals("TESTSUBJECT", td.getText());
        td = driver.findElement(By.cssSelector("td.bbsNick"));
        assertEquals("TEST", td.getText());
        td = driver.findElement(By.cssSelector("td.date"));
        assertEquals("2017/01/16", td.getText());
        td = driver.findElement(By.cssSelector("td.hit"));
        assertEquals("2", td.getText());

        query = "TRUNCATE TABLE post;" ;
        stmt.executeUpdate(query);
    }

    @After
    public void tearDown() {
        this.driver.quit();
    }
}
package com.ioio.jsontools.core.service;

import com.ioio.jsontools.core.CoreApp;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.preemptive;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CoreApp.class }, webEnvironment = RANDOM_PORT)
public class APIControllerV1Test {

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        //RestAssured.authentication = preemptive().basic("admin", "passw0rd");
    }

    @Test
    public void shouldSayHello() {
        get("http://localhost:" + port + "/api/v1/test")
                .then()
                .assertThat()
                .statusCode(200);
        // gergre
        //        .body("firstname", Matchers.equalTo("John"))
        //        .and()
         //       .body("lastname", Matchers.equalTo("Smith"));
    }
}

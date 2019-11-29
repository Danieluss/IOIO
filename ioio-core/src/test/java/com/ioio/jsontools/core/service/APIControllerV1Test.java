package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.CoreApp;
import com.ioio.jsontools.core.service.filter.Filter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CoreApp.class }, webEnvironment = RANDOM_PORT)
public class APIControllerV1Test {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void shouldSayPongForOurPing() {
        get("http://localhost:" + port + "/api/ping")
                .then()
                .assertThat()
                .statusCode(200)
                .body(equalTo("Server responded properly."));
    }

    @Test
    public void shouldShouldMaxifier() {
        String payload = "{\"obj\": {\"arr\": [{ \"def\": 999}, { \"def\": 112}], \"xyz\": \"value\"}}";
        String expected = "{\n  \"obj\" : {\n    \"arr\" : [ {\n      \"def\" : 999\n    }, {\n      \"def\" : 112\n    } ],\n    \"xyz\" : \"value\"\n  }\n}";

        String result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("http://localhost:" + port + "/api/v1/modifier/maxifier")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        assertEquals(result, expected);
    }

    // System.out.println("Response Body is =>  " +
    // whitelist, blacklist, maxifier, minifier
}

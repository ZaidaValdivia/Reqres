package com.talent;

import static io.restassured.RestAssured.*;

import com.talent.model.Register;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.*;
import static io.restassured.path.json.JsonPath.from;

public class PostRegisterTest {

    @Before
    public void setup() {
        baseURI = "https://reqres.in";
        basePath = "/api";
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    @Test
    public void postRegisterValidTest() {

        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .post("/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    public void postRegisterInvalidPasswordTest() {

        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"123456\"\n" +
                        "}")
                .post("/register")
                .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void postRegisterNotDefinedUserTest() {

        String response = given()
                .when()
                .body("{\n" +
                        "    \"email\": \"juan.perez@gmail.in\",\n" +
                        "    \"password\": \"123456789\"\n" +
                        "}")
                .post("/register")
                .then().extract().body().asString();

        Register register = from(response).getObject("", Register.class);
        System.out.println(register.getId());
        System.out.println(register.getToken());
    }

    @Test
    public void postRegisterValidTest2() {

        String response = given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .post("/register")
                .then().extract().body().asString();

        Register register = from(response).getObject("", Register.class);
        assertThat( register.getId(), equalTo(4));
        System.out.println(register.getId());
        System.out.println(register.getToken());
    }

    @Test
    public void postRegisterInvalidPasswordTest1() {

        given()
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", notNullValue());
    }
}


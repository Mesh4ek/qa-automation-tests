package com.qa.automation.api.client;

import com.qa.automation.config.TestConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

abstract class BaseClient {

    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri(TestConfig.bookerBaseUri())
            .setContentType(ContentType.JSON)
            // Restful Booker answers 418 unless Accept is exactly "application/json".
            .setAccept("application/json")
            .setConfig(RestAssuredConfig.config()
                    .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                            .defaultObjectMapperType(ObjectMapperType.JACKSON_3))
                    .logConfig(LogConfig.logConfig()
                            .enableLoggingOfRequestAndResponseIfValidationFails()))
            .build();

    protected RequestSpecification request() {
        return given().spec(REQUEST_SPEC);
    }
}
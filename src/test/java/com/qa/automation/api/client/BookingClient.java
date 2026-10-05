package com.qa.automation.api.client;

import com.qa.automation.api.model.Booking;
import com.qa.automation.config.Credentials;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.notNullValue;

public final class BookingClient extends BaseClient {

    private static final String AUTH = "/auth";
    private static final String BOOKINGS = "/booking";
    private static final String BOOKING_BY_ID = "/booking/{id}";

    public String createToken(Credentials credentials) {
        return request()
                .body(credentials)
                .post(AUTH)
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .path("token");
    }

    public Response create(Booking booking) {
        return request()
                .body(booking)
                .post(BOOKINGS);
    }

    public Response getById(int bookingId) {
        return request()
                .pathParam("id", bookingId)
                .get(BOOKING_BY_ID);
    }

    public Response delete(int bookingId, String token) {
        return request()
                .cookie("token", token)
                .pathParam("id", bookingId)
                .delete(BOOKING_BY_ID);
    }

    public Response deleteWithoutToken(int bookingId) {
        return request()
                .pathParam("id", bookingId)
                .delete(BOOKING_BY_ID);
    }
}

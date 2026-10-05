package com.qa.automation.api;

import com.qa.automation.api.client.BookingClient;
import com.qa.automation.api.model.Booking;
import com.qa.automation.api.model.BookingDates;
import com.qa.automation.api.model.CreatedBooking;
import com.qa.automation.config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
class BookingApiTest {

    private static final int NON_EXISTENT_BOOKING_ID = 999_999_999;

    private final BookingClient bookingClient = new BookingClient();
    private final List<Integer> createdBookingIds = new ArrayList<>();

    @AfterEach
    void deleteCreatedBookings() {
        if (createdBookingIds.isEmpty()) {
            return;
        }
        String token = bookingClient.createToken(TestConfig.bookerAdmin());
        // Restful Booker answers a successful DELETE with 201 Created.
        createdBookingIds.forEach(id -> bookingClient.delete(id, token).then().statusCode(201));
    }

    @Test
    void createdBookingCanBeRetrievedById() {
        Booking booking = validBooking();

        CreatedBooking created = createBooking(booking);

        assertThat(created.bookingId()).as("created booking id").isPositive();
        assertThat(created.booking())
                .as("booking returned by POST /booking")
                .usingRecursiveComparison()
                .isEqualTo(booking);

        Booking retrieved = bookingClient.getById(created.bookingId())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(Booking.class);

        assertThat(retrieved)
                .as("booking returned by GET /booking/%d", created.bookingId())
                .usingRecursiveComparison()
                .isEqualTo(booking);
    }

    @Test
    void gettingNonExistentBookingReturnsNotFound() {
        bookingClient.getById(NON_EXISTENT_BOOKING_ID)
                .then()
                .statusCode(404);
    }

    @Test
    void bookingCannotBeDeletedWithoutToken() {
        CreatedBooking created = createBooking(validBooking());

        bookingClient.deleteWithoutToken(created.bookingId())
                .then()
                .statusCode(403);

        bookingClient.getById(created.bookingId())
                .then()
                .statusCode(200);
    }

    private CreatedBooking createBooking(Booking booking) {
        CreatedBooking created = bookingClient.create(booking)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(CreatedBooking.class);
        createdBookingIds.add(created.bookingId());
        return created;
    }

    private static Booking validBooking() {
        LocalDate checkin = LocalDate.now().plusDays(30);
        return new Booking(
                "Test",
                "User-" + UUID.randomUUID().toString().substring(0, 8),
                ThreadLocalRandom.current().nextInt(100, 1000),
                true,
                new BookingDates(checkin, checkin.plusDays(5)),
                "Breakfast");
    }
}

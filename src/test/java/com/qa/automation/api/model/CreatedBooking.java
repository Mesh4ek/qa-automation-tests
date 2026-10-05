package com.qa.automation.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatedBooking(
        @JsonProperty("bookingid") int bookingId,
        @JsonProperty("booking") Booking booking) {
}
package com.qa.automation.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record BookingDates(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate checkin,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate checkout) {
}
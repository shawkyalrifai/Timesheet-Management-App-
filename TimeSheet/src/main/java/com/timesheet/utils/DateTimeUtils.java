package com.timesheet.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtils {

    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd");
        }
    }

    public static LocalDateTime parseToDateTime(LocalDate date, String input) {
        try {
            if (input == null || input.isEmpty()) return null;
            if (input.contains("T")) {
                return LocalDateTime.parse(input);
            }
            LocalTime time = LocalTime.parse(input);
            return LocalDateTime.of(date, time);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid time format, use HH:mm or ISO datetime");
        }
    }

}

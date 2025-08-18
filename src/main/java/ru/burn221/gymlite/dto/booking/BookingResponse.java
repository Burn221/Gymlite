package ru.burn221.gymlite.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.burn221.gymlite.model.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponse(
        Integer id,
        Integer equipmentId,
        String userName,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime,
        BigDecimal finalPrice,
        BookingStatus bookingStatus,
        LocalDateTime createdAt
) {
}

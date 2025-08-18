package ru.burn221.gymlite.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import ru.burn221.gymlite.model.BookingStatus;

import java.time.LocalDateTime;

public record BookingUpdateRequest(
        @NotNull @Positive Integer bookingId,
        @NotNull @Positive Integer equipmentId,
        @NotBlank @Size(max=100) String userName,
        @NotNull @FutureOrPresent @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
        @NotNull @Future @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime,
        @NotNull BookingStatus bookingStatus

        ) {
}

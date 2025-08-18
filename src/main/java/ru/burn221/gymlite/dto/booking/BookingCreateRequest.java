package ru.burn221.gymlite.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record BookingCreateRequest(
        @NotNull @Positive Integer equipmentId,
        @NotBlank @Size(max= 100) String userName,

        @NotNull @FutureOrPresent @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
        @NotNull @Future @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime



) {
}

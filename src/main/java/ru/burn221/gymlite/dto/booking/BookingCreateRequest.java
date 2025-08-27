package ru.burn221.gymlite.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record BookingCreateRequest(
        @Schema(example = "1") @NotNull @Positive Integer equipmentId,

        @Schema(example = "Nikita Nevmyvaka") @NotBlank @Size(max= 100) String userName,

        @Schema(example = "2025-08-29T21:00") @NotNull @FutureOrPresent @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,

        @Schema(example = "2025-08-29T22:00") @NotNull @Future @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime




) {
}

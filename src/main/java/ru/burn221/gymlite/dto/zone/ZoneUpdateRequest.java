package ru.burn221.gymlite.dto.zone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ZoneUpdateRequest(
        @Schema(example = "1")@NotNull @Positive Integer id,

        @NotBlank @Size(max=100) String zoneName,

        @Size(max = 500) String description,

        @NotNull Boolean active
) {
}

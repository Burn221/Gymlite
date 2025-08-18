package ru.burn221.gymlite.dto.zone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ZoneUpdateRequest(
        @NotNull @Positive Integer zoneId,
        @NotBlank @Size(max=100) String zoneName,
        @Size(max = 500) String description,
        @NotNull Boolean active
) {
}

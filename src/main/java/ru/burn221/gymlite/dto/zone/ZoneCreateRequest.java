package ru.burn221.gymlite.dto.zone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ZoneCreateRequest(
        @Schema(example = "Cardio") @NotBlank @Size(max=100) String zoneName,

        @Schema(example = "Zone for cardio exercises") @Size(max = 500) String description,

        @Schema(example = "true")@NotNull Boolean active
){}



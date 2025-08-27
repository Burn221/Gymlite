package ru.burn221.gymlite.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record EquipmentCreateRequest(
        @Schema(example = "1") @NotNull @Positive Integer zoneId,

        @Schema(example = "barbell") @NotBlank @Size(max=100) String equipmentName,

        @Schema(example = "equipment for bench") @Size(max=500) String description,

        @Schema(example = "100.00",description = "price per hour")@NotNull @DecimalMin(value = "0.00") @Digits(integer = 10, fraction = 2) BigDecimal price,

        @Schema(example = "true") @NotNull Boolean active


        ) {
}

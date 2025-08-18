package ru.burn221.gymlite.dto.equipment;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record EquipmentCreateRequest(
        @NotNull @Positive Integer zoneId,
        @NotBlank @Size(max=100) String equipmentName,
        @Size(max=500) String description,
        @NotNull @DecimalMin(value = "0.00") @Digits(integer = 10, fraction = 2) BigDecimal price,
        @NotNull Boolean active


        ) {
}

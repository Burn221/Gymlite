package ru.burn221.gymlite.dto.equipment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EquipmentResponse(
        Integer id,
        Integer zoneId,
        String equipmentName,
        String description,
        BigDecimal price,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

package ru.burn221.gymlite.dto.zone;

import java.time.LocalDateTime;

public record ZoneResponse(
        Integer id,
        String zoneName,
        String description,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

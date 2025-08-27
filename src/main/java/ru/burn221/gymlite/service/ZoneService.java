package ru.burn221.gymlite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.burn221.gymlite.dto.zone.ZoneCreateRequest;
import ru.burn221.gymlite.dto.zone.ZoneResponse;
import ru.burn221.gymlite.dto.zone.ZoneUpdateRequest;

public interface ZoneService {

    ZoneResponse createZone(ZoneCreateRequest dto);

    ZoneResponse updateZone(ZoneUpdateRequest dto);

    ZoneResponse deactivateZone(Integer id);

    ZoneResponse activateZone(Integer id);

    void deleteZone(Integer id);

    ZoneResponse getActiveZoneById(Integer id);

    ZoneResponse getZoneById(Integer id );

    ZoneResponse getZone(String zoneName);

    Page<ZoneResponse> getAllZones(Pageable pageable);

    Page<ZoneResponse> getAllActiveZones(Pageable pageable);


}

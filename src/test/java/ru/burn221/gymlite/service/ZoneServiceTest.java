package ru.burn221.gymlite.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.burn221.gymlite.dto.zone.ZoneCreateRequest;
import ru.burn221.gymlite.dto.zone.ZoneResponse;
import ru.burn221.gymlite.dto.zone.ZoneUpdateRequest;
import ru.burn221.gymlite.exceptions.ConflictException;
import ru.burn221.gymlite.mapper.ZoneMapper;
import ru.burn221.gymlite.model.Zone;
import ru.burn221.gymlite.repository.ZoneRepository;
import ru.burn221.gymlite.service.impl.ZoneServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ZoneServiceTest {

    @Mock
    private ZoneRepository repository;

    @Mock
    private ZoneMapper mapper;

    @InjectMocks
    private ZoneServiceImpl service;


    @Test
    @DisplayName("Create zone should verify creating")
    void createZone_ShouldVerifyCreating(){
        var zone = new Zone();
        zone.setId(1);
        zone.setZoneName("zone");
        zone.setDescription("desc");
        zone.setActive(true);
        zone.setCreatedAt(null);
        zone.setUpdatedAt(null);

        var req= new ZoneCreateRequest("zone","desc",true);
        var resp= new ZoneResponse(1,"zone","desc",true,null,null);

        when(mapper.toEntity(req)).thenReturn(zone);
        when(repository.save(zone)).thenReturn(zone);
        when(mapper.toResponse(zone)).thenReturn(resp);

        var entityResponse= service.createZone(req);

        assertNotNull(entityResponse);
        assertEquals("zone",entityResponse.zoneName());
        verify(repository).save(zone);

    }

    @Test
    void createZone_ThrowsConflictException(){
        var req= new ZoneCreateRequest("zone","desc",true);

        when(repository.existsByZoneNameIgnoreCase("zone")).thenReturn(true);

        assertThrows(ConflictException.class, ()-> service.createZone(req));

        verify(repository).existsByZoneNameIgnoreCase("zone");
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void updateZone_ReturnsValidResponse(){
        var req= new ZoneUpdateRequest(1,"zone","desc",true);

        var zone = new Zone();
        zone.setId(1);
        zone.setZoneName("zone");
        zone.setDescription("desc");
        zone.setActive(true);
        zone.setCreatedAt(null);
        zone.setUpdatedAt(null);

        var resp= new ZoneResponse(1,"zone","desc",true,null,null);

        when(repository.findById(1)).thenReturn(Optional.of(zone));
        when(repository.save(zone)).thenReturn(zone);
        when(mapper.toResponse(zone)).thenReturn(resp);

        var entityResponse= service.updateZone(req);

        assertNotNull(entityResponse);
        assertEquals("zone",entityResponse.zoneName());
        verify(repository).save(zone);
    }

    @Test
    void getAllZones_ReturnsValidResponse(){
        Zone z1 = new Zone();
        z1.setId(1);
        z1.setZoneName("cardio");
        z1.setDescription("d1");
        z1.setActive(true);

        Zone z2 = new Zone();
        z2.setId(2);
        z2.setZoneName("lift");
        z2.setDescription("d2");
        z2.setActive(true);

        Page<Zone> zonesPage = new PageImpl<>(List.of(z1, z2), Pageable.unpaged(),2);
        when(repository.findAll(Pageable.unpaged())).thenReturn(zonesPage);
        when(mapper.toResponse(z1)).thenReturn(new ZoneResponse(1, "cardio", "d1", true, null, null));
        when(mapper.toResponse(z2)).thenReturn(new ZoneResponse(2, "lift", "d2", true, null, null));


        Page<ZoneResponse> result = service.getAllZones(Pageable.unpaged());


        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("cardio", result.getContent().get(0).zoneName());
        assertEquals("lift", result.getContent().get(1).zoneName());
    }
}

package ru.burn221.gymlite.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.MediaType;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.burn221.gymlite.dto.equipment.EquipmentResponse;
import ru.burn221.gymlite.dto.zone.ZoneCreateRequest;
import ru.burn221.gymlite.dto.zone.ZoneResponse;

import ru.burn221.gymlite.controller.ZoneController;
import ru.burn221.gymlite.dto.zone.ZoneUpdateRequest;
import ru.burn221.gymlite.mapper.ZoneMapper;
import ru.burn221.gymlite.model.Zone;
import ru.burn221.gymlite.repository.ZoneRepository;
import ru.burn221.gymlite.service.impl.ZoneServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ZoneControllerTest {

    @Mock
     ZoneServiceImpl service;

    @Mock
    ZoneRepository repository;

    @Mock
    ZoneMapper mapper;




    @InjectMocks
     ZoneController controller;

    @Test
    void createZone_ReturnsValidResponseEntity(){

        var req= new ZoneCreateRequest("zone","desc",true);

        var entity= new Zone();
        entity.setId(1);
        entity.setZoneName("zone");
        entity.setDescription("desc");
        entity.setActive(true);
        entity.setCreatedAt(null);
        entity.setUpdatedAt(null);

        var resp= new ZoneResponse(1,"zone","desc",true,null,null);


        when(service.createZone(req)).thenReturn(resp);



        var response= controller.create(req);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());


        assertSame("zone",response.getBody().zoneName());

        verify(service).createZone(req);

    }

    @Test
    void getAllZones_ReturnsValidResponseEntity(){

        var page= new PageImpl<>(List.of(
                new ZoneResponse(1,"cardio","desc", true,null,null),
                new ZoneResponse(2,"lift","desc", true,null,null)
        ));
        when(service.getAllActiveZones(Pageable.unpaged())).thenReturn(page);

        var responseEntity= controller.list(true, Pageable.unpaged());

        assertNotNull(responseEntity);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        assertEquals(page,responseEntity.getBody());

    }

    @Test
    void updateZone_ReturnsValidResponseEntity(){
        var req= new ZoneUpdateRequest(1,"zone","desc",true);
        var resp= new ZoneResponse(1,"zone","desc",false,null,null);

        when(service.updateZone(req)).thenReturn(resp);

        var entityResponse= controller.update(1,req);

        assertNotNull(entityResponse);

        assertEquals(HttpStatus.OK,entityResponse.getStatusCode());

        assertEquals(entityResponse.getBody().active(),resp.active());

        verify(service).updateZone(req);
    }

    @Test
    void deactivateZone_ReturnsValidresponseEntity(){
        var response= new ZoneResponse(1,"zone","desc",false,null,null);

        when(service.deactivateZone(1)).thenReturn(response);

        var responseEntity= controller.deactivate(1);

        assertNotNull(responseEntity);
        assertEquals(false,responseEntity.getBody().active());
    }

    @Test
    void activateZone_ReturnsValidResponse(){
        var response = new ZoneResponse(1,  "equip", "desc",  true, null, null);

        when(service.activateZone(1)).thenReturn(response);

        var responseEntity= controller.activate(1);

        assertNotNull(responseEntity);
    }
}

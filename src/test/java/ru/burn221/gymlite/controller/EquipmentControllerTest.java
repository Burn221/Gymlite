package ru.burn221.gymlite.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.burn221.gymlite.dto.equipment.EquipmentCreateRequest;
import ru.burn221.gymlite.dto.equipment.EquipmentResponse;
import ru.burn221.gymlite.dto.equipment.EquipmentUpdateRequest;
import ru.burn221.gymlite.dto.zone.ZoneResponse;
import ru.burn221.gymlite.model.Zone;
import ru.burn221.gymlite.service.impl.EquipmentServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentControllerTest {
    @Mock
    EquipmentServiceImpl service;

    @InjectMocks
    EquipmentController controller;


    @Test
    void createEquipment_ReturnsValidResponse() {
        var entity = new Zone();
        entity.setId(1);
        entity.setZoneName("zone");
        entity.setDescription("desc");
        entity.setActive(true);
        entity.setCreatedAt(null);
        entity.setUpdatedAt(null);

        var req = new EquipmentCreateRequest(1, "equip", "desc", BigDecimal.valueOf(120.00), true);

        var response = new EquipmentResponse(1, 1, "equip", "desc", BigDecimal.valueOf(120.00), true, null, null);

        when(service.createEquipment(req)).thenReturn(response);

        var entityResponse = controller.create(req);

        assertNotNull(entityResponse);
        assertEquals(HttpStatus.CREATED, entityResponse.getStatusCode());


        assertSame("equip", entityResponse.getBody().equipmentName());

        verify(service).createEquipment(req);
    }

    @Test
    void updateEquipment_ReturnsValidResponse() {
        var entity = new Zone();
        entity.setId(1);
        entity.setZoneName("zone");
        entity.setDescription("desc");
        entity.setActive(true);
        entity.setCreatedAt(null);
        entity.setUpdatedAt(null);

        var req = new EquipmentUpdateRequest(1, 1, "equip", "desc", BigDecimal.valueOf(120.00), true);

        var response = new EquipmentResponse(1, 1, "equip", "desc", BigDecimal.valueOf(120.00), true, null, null);

        when(service.updateEquipment(req)).thenReturn(response);

        var entityResponse = controller.update(1, req);

        assertNotNull(entityResponse);

        assertEquals(HttpStatus.OK, entityResponse.getStatusCode());

        assertEquals(entityResponse.getBody().active(), response.active());

        verify(service).updateEquipment(req);
    }

    @Test
    void getAllEquipment_ReturnsValidResponse() {
        var page = new PageImpl<>(List.of(
                new EquipmentResponse(1, 1, "equip", "desc", BigDecimal.valueOf(120.00), true, null, null),
                new EquipmentResponse(2, 1, "equ", "desc", BigDecimal.valueOf(120.00), true, null, null)));

        when(service.getAllActiveEquipment(Pageable.unpaged())).thenReturn(page);
        when(service.getAllEquipment(Pageable.unpaged())).thenReturn(page);

        var responseEntity= controller.list(true,Pageable.unpaged());
        var responseEntityFalse= controller.list(false,Pageable.unpaged());

        assertNotNull(responseEntity);
        assertNotNull(responseEntityFalse);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        assertEquals(page,responseEntity.getBody());

        assertEquals(HttpStatus.OK,responseEntityFalse.getStatusCode());

        assertEquals(page,responseEntityFalse.getBody());

    }

    @Test
    void deactivateEquipment_ReturnsValidResponse(){
        var response = new EquipmentResponse(1, 1, "equip", "desc", BigDecimal.valueOf(120.00), true, null, null);

        when(service.deactivateEquipment(1)).thenReturn(response);

        var responseEntity= controller.deactivate(1);

        assertNotNull(responseEntity);
    }

    @Test
    void activateEquipment_ReturnsValidResponse(){
        var response = new EquipmentResponse(1, 1, "equip", "desc", BigDecimal.valueOf(120.00), true, null, null);

        when(service.activateEquipment(1)).thenReturn(response);

        var responseEntity= controller.activate(1);

        assertNotNull(responseEntity);
    }
}
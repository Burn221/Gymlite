package ru.burn221.gymlite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.burn221.gymlite.dto.equipment.EquipmentCreateRequest;
import ru.burn221.gymlite.dto.equipment.EquipmentResponse;
import ru.burn221.gymlite.dto.equipment.EquipmentUpdateRequest;
import ru.burn221.gymlite.service.impl.EquipmentServiceImpl;

import java.net.URI;

@Tag(name = "Equipment", description = "Gym equipment control")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipment")
@Validated
public class EquipmentController {

    private final EquipmentServiceImpl equipmentServiceImpl;

    @Operation(summary = "Create equipment")
    @PostMapping
    public ResponseEntity<EquipmentResponse> create(@RequestBody @Valid EquipmentCreateRequest request){
        EquipmentResponse created= equipmentServiceImpl.createEquipment(request);

        return ResponseEntity.created(URI.create("/api/v1/equipment/"+created.id()))
                .body(created);

    }

    @Operation(summary = "Update equipment")
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponse> update(@PathVariable Integer id
            , @RequestBody @Valid EquipmentUpdateRequest request){

        EquipmentUpdateRequest fixed= new EquipmentUpdateRequest(
                id,
                request.zoneId(),
                request.equipmentName(),
                request.description(),
                request.price(),
                request.active()
        );

        EquipmentResponse updated= equipmentServiceImpl.updateEquipment(fixed);

        return ResponseEntity.ok(updated);

    }

    @Operation(summary = "Deactivate equipment")
    @PatchMapping ("/deactivate/{id}")
    public ResponseEntity<EquipmentResponse> deactivate (@PathVariable Integer id){
        EquipmentResponse deactivated= equipmentServiceImpl.deactivateEquipment(id);

        return ResponseEntity.ok(deactivated);
    }

    @Operation(summary = "Activate equipment")
    @PatchMapping ("/activate/{id}")
    public ResponseEntity<EquipmentResponse> activate (@PathVariable Integer id){
        EquipmentResponse activated= equipmentServiceImpl.activateEquipment(id);

        return ResponseEntity.ok(activated);
    }

    @Operation(summary = "Delete equipment")
    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        equipmentServiceImpl.deleteEquipment(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get equipment by Id")
    @GetMapping ("/{id}")
    public ResponseEntity<EquipmentResponse>  getById(@PathVariable Integer id){
        return ResponseEntity.ok(equipmentServiceImpl.getEquipmentById(id));
    }

    @Operation(summary = "Get active equipment by Id")
    @GetMapping ("/{id}/active")
    public ResponseEntity<EquipmentResponse>  getByIdActive(@PathVariable Integer id){
        return ResponseEntity.ok(equipmentServiceImpl.getActiveEquipmentById(id));
    }

    @Operation(summary = "Get all equipment by zone")
    @GetMapping ("/{zoneId}/all-equipment-zone")
    public ResponseEntity<Page<EquipmentResponse>> allEquipmentByZone(@PathVariable Integer zoneId,
            @ParameterObject @PageableDefault(size = 20, sort = "id")Pageable pageable
            ){
        Page<EquipmentResponse> page= equipmentServiceImpl.getEquipmentByZoneId(zoneId,pageable);

        return  ResponseEntity.ok(page);

    }

    @Operation(summary = "Get all active equipment by zone")
    @GetMapping ("/{zoneId}/all-equipment-zone-active")
    public ResponseEntity<Page<EquipmentResponse>> getAllActiveEquipmentByZone(@PathVariable Integer zoneId
            , @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable){

        Page<EquipmentResponse> page= equipmentServiceImpl.getActiveEquipmentByZoneId(zoneId,pageable);

        return  ResponseEntity.ok(page);

    }

    @Operation(summary = "Get all equipment",description = "Returns all equipment if  value=false, returns all active equipment if value=true")
    @GetMapping
    public ResponseEntity<Page<EquipmentResponse>> list(
            @RequestParam(value = "active", required = false) Boolean active,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable)
    {
        Page<EquipmentResponse> page= (active!=null && active)
                ? equipmentServiceImpl.getAllActiveEquipment(pageable)
                : equipmentServiceImpl.getAllEquipment(pageable);

        return ResponseEntity.ok(page);
    }

}

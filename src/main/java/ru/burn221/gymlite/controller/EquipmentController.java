package ru.burn221.gymlite.controller;

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
import ru.burn221.gymlite.service.EquipmentService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipment")
@Validated
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentResponse> create(@RequestBody @Valid EquipmentCreateRequest request){
        EquipmentResponse created= equipmentService.createEquipment(request);

        return ResponseEntity.created(URI.create("/api/equipment/"+created.id()))
                .body(created);

    }

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

        EquipmentResponse updated= equipmentService.updateEquipment(fixed);

        return ResponseEntity.ok(updated);

    }

    @PatchMapping ("/deactivate/{id}")
    public ResponseEntity<EquipmentResponse> deactivate (@PathVariable Integer id){
        EquipmentResponse deactivated= equipmentService.deactivateEquipment(id);

        return ResponseEntity.ok(deactivated);
    }

    @PatchMapping ("/activate/{id}")
    public ResponseEntity<EquipmentResponse> activate (@PathVariable Integer id){
        EquipmentResponse activated= equipmentService.activateEquipment(id);

        return ResponseEntity.ok(activated);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        equipmentService.deleteEquipment(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<EquipmentResponse>  getById(@PathVariable Integer id){
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping ("/{id}/active")
    public ResponseEntity<EquipmentResponse>  getByIdActive(@PathVariable Integer id){
        return ResponseEntity.ok(equipmentService.getActiveEquipmentById(id));
    }

    @GetMapping ("/{zoneId}/all-equipment-zone")
    public ResponseEntity<Page<EquipmentResponse>> allEquipmentByZone(@PathVariable Integer zoneId,
            @ParameterObject @PageableDefault(size = 20, sort = "id")Pageable pageable
            ){
        Page<EquipmentResponse> page= equipmentService.getEquipmentByZoneId(zoneId,pageable);

        return  ResponseEntity.ok(page);

    }

    @GetMapping ("/{zoneId}/all-equipment-zone-active")
    public ResponseEntity<Page<EquipmentResponse>> getAllActiveEquipmentByZone(@PathVariable Integer zoneId
            , @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable){

        Page<EquipmentResponse> page= equipmentService.getActiveEquipmentByZoneId(zoneId,pageable);

        return  ResponseEntity.ok(page);

    }

    @GetMapping
    public ResponseEntity<Page<EquipmentResponse>> list(
            @RequestParam(value = "active", required = false) Boolean active,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable)
    {
        Page<EquipmentResponse> page= (active!=null && active)
                ? equipmentService.getAllActiveEquipment(pageable)
                : equipmentService.getAllEquipment(pageable);

        return ResponseEntity.ok(page);
    }

}

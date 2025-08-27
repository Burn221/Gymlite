package ru.burn221.gymlite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import ru.burn221.gymlite.dto.zone.ZoneCreateRequest;
import ru.burn221.gymlite.dto.zone.ZoneResponse;
import ru.burn221.gymlite.dto.zone.ZoneUpdateRequest;
import ru.burn221.gymlite.service.impl.ZoneServiceImpl;

import java.net.URI;

@Tag(name = "Zones", description = "Gym zones control")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zones")
@Validated
public class ZoneController {
    private final ZoneServiceImpl zoneServiceImpl;

    @Operation(summary = "Creating new zone")
    @PostMapping
    public ResponseEntity<ZoneResponse> create(@RequestBody @Valid ZoneCreateRequest request) {
        ZoneResponse created = zoneServiceImpl.createZone(request);

        return ResponseEntity
                .created(URI.create("/api/v1/zones/" + created.id()))
                .body(created);
    }


    @Operation(summary = "Update existing zone")
    @PutMapping("/{id}")
    public ResponseEntity<ZoneResponse> update(
            @PathVariable Integer id,
            @RequestBody @Valid ZoneUpdateRequest request) {

        ZoneUpdateRequest fixed = new ZoneUpdateRequest(
                id,
                request.zoneName(),
                request.description(),
                request.active()
        );

        ZoneResponse updated = zoneServiceImpl.updateZone(fixed);

        return ResponseEntity.ok(updated);

    }


    @Operation(summary = "Get active zone by Id", description = "Return active zone by id, receives zone id on input")
    @GetMapping("/{id}/active-zone")
    public ResponseEntity<ZoneResponse> getByIdActive(@PathVariable Integer id) {
        return ResponseEntity.ok(zoneServiceImpl.getActiveZoneById(id));
    }

    @Operation(summary = "Get zone by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(zoneServiceImpl.getZoneById(id));
    }

    @Operation(summary = "Get zone by zone name")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<ZoneResponse> getByName(@PathVariable String name) {
        return ResponseEntity.ok(zoneServiceImpl.getZone(name));
    }

    @Operation(summary = "Get all zones", description = "Returns all zones if  value=false, returns all active zones if value=true")
    @GetMapping
    public ResponseEntity<Page<ZoneResponse>> list(
            @RequestParam(value = "active", required = false) Boolean active,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {

        Page<ZoneResponse> page = (active != null && active)
                ? zoneServiceImpl.getAllActiveZones(pageable)
                : zoneServiceImpl.getAllZones(pageable);

        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Deactivate zone")
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<ZoneResponse> deactivate(@PathVariable Integer id) {
        return ResponseEntity.ok(zoneServiceImpl.deactivateZone(id));
    }

    @Operation(summary = "Activate zone")
    @PatchMapping("/activate/{id}")
    public ResponseEntity<ZoneResponse> activate(@PathVariable Integer id) {
        return ResponseEntity.ok(zoneServiceImpl.activateZone(id));
    }

    @Operation(summary = "Delete zone", description = "Deletes zone if deactivated")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        zoneServiceImpl.deleteZone(id);

        return ResponseEntity.noContent().build();
    }


}

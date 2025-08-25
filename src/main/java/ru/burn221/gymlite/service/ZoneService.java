package ru.burn221.gymlite.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.burn221.gymlite.dto.zone.ZoneCreateRequest;
import ru.burn221.gymlite.dto.zone.ZoneResponse;
import ru.burn221.gymlite.dto.zone.ZoneUpdateRequest;
import ru.burn221.gymlite.exceptions.ConflictException;
import ru.burn221.gymlite.exceptions.NotFoundException;
import ru.burn221.gymlite.mapper.ZoneMapper;
import ru.burn221.gymlite.model.Zone;
import ru.burn221.gymlite.repository.EquipmentRepository;
import ru.burn221.gymlite.repository.ZoneRepository;

@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository zoneRepository;
    private final EquipmentRepository equipmentRepository;
    private final ZoneMapper zoneMapper;

    @Transactional
    public ZoneResponse createZone(ZoneCreateRequest dto){
        String normalizedName= dto.zoneName().trim();
        if(zoneRepository.existsByZoneNameIgnoreCase(normalizedName)){
            throw new ConflictException("This zone already exists "+ normalizedName);

        }
        Zone zone= zoneMapper.toEntity(dto);

        Zone save= zoneRepository.save(zone);

        return zoneMapper.toResponse(save);

    }

    public ZoneResponse getZone(String zoneName){
        String normalizedName= zoneName.trim();
        Zone zone=zoneRepository.findByZoneNameIgnoreCase(normalizedName)
                .orElseThrow(()-> new NotFoundException("Zone "+normalizedName+" not found "));
        return zoneMapper.toResponse(zone);
    }

    public Page<ZoneResponse> getAllZones(Pageable pageable){
        return zoneRepository.findAll(pageable)
                .map(zoneMapper::toResponse);
    }
    @Transactional
    public ZoneResponse updateZone(ZoneUpdateRequest dto){
        String normalizedName= dto.zoneName().trim();
        Zone zone= zoneRepository.findById(dto.id())
                .orElseThrow(()->new NotFoundException("Zone "+normalizedName+" not found "));

        if (!zone.getZoneName().equalsIgnoreCase(normalizedName)
                && zoneRepository.existsByZoneNameIgnoreCase(normalizedName)) {
            throw new ConflictException("Zone with name '" + normalizedName + "' already exists");
        }

        zoneMapper.update(zone,dto);
        Zone saved= zoneRepository.save(zone);
        return zoneMapper.toResponse(saved);
    }

    public ZoneResponse deactivateZone(Integer id){
        Zone zone= zoneRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Zone with id "+id+" not found"));
        zone.setActive(false);

        return zoneMapper.toResponse(zoneRepository.save(zone));
    }

    public ZoneResponse activateZone(Integer id){
        Zone zone= zoneRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Zone with id "+id+" not found"));
        zone.setActive(true);

        return zoneMapper.toResponse(zoneRepository.save(zone));
    }

    public ZoneResponse getActiveZoneById(Integer id ){

        Zone zone= zoneRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new NotFoundException("Zone with id "+id+" not found"));

        return zoneMapper.toResponse(zone);

    }

    public ZoneResponse getZoneById(Integer id ){

        Zone zone= zoneRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Zone with id "+id+" not found"));

        return zoneMapper.toResponse(zone);

    }



    public Page<ZoneResponse> getAllActiveZones(Pageable pageable){
        return zoneRepository.findByActiveTrue(pageable)
                .map(zoneMapper::toResponse);

    }
    @Transactional
    public void deleteZone(Integer id){
        if(equipmentRepository.existsByZone_Id(id)){
            throw new ConflictException("Zone with id "+id+" has existing equipment");
        }
        else{
            zoneRepository.deleteById(id);

        }
    }


}

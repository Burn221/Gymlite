package ru.burn221.gymlite.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    public Zone createZone(String zoneName, String description, boolean active){
        if(zoneRepository.existsByZoneNameIgnoreCase(zoneName)){
            throw new IllegalArgumentException("This zone already exists "+ zoneName);

        }
        Zone zone= new Zone();

        zone.setZoneName(zoneName);
        zone.setDescription(description);
        zone.setActive(active);
        return zoneRepository.save(zone);
    }

    public Zone getZone(String zoneName){
        return zoneRepository.findByZoneNameIgnoreCase(zoneName)
                .orElseThrow(()-> new RuntimeException("Zone "+zoneName+" not found "));
    }

    public Page<Zone> getAllZones(Pageable pageable){
        return zoneRepository.findAll(pageable);
    }
    @Transactional
    public Zone updateZone(Integer zoneId,String zoneName, String description, boolean active){

        Zone zone= zoneRepository.findById(zoneId)
                .orElseThrow(()->new RuntimeException("Zone "+zoneName+" not found "));

        zone.setZoneName(zoneName.trim());
        zone.setDescription(description);
        zone.setActive(active);
        return zoneRepository.save(zone);
    }

    public Zone deactivateZone(Integer id){
        Zone zone= zoneRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Zone with id "+id+" not found"));
        zone.setActive(false);

        return zoneRepository.save(zone);
    }

    public Zone activateZone(Integer id){
        Zone zone= zoneRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Zone with id "+id+" not found"));
        zone.setActive(true);

        return zoneRepository.save(zone);
    }

    public Zone getActiveZoneById(Integer id ){

        return zoneRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()->new RuntimeException("Zone with id "+id+" not found"));

    }

    public Page<Zone> getAllActiveZones(Pageable pageable){
        return zoneRepository.findByActiveTrue(pageable);

    }
    @Transactional
    public void deleteZone(Integer id){
        if(equipmentRepository.existsByZone_Id(id)){
            throw new RuntimeException("This zone has existing equipment");
        }
        else{
            zoneRepository.deleteById(id);

        }
    }


}

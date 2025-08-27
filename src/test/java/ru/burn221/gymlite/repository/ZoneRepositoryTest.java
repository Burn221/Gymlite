package ru.burn221.gymlite.repository;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.burn221.gymlite.dto.zone.ZoneResponse;
import ru.burn221.gymlite.model.Zone;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.burn221.gymlite.model.Zone;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ZoneRepositoryTest {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    @DisplayName("find by zone name test should return Zone by name ignoring case")
    void findByZoneName_ShouldReturnZoneByName(){
        Zone zone= new Zone();
        zone.setZoneName("Cardio");
        zone.setDescription("rgeger");
        zone.setActive(true);

        testEntityManager.persistAndFlush(zone);

        Optional<Zone> actual= zoneRepository.findByZoneNameIgnoreCase("cardio");

        assertNotNull(actual);
        assertEquals(actual.get().getZoneName(),zone.getZoneName());

    }






}



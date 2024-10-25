package tn.esprit.tpfoyer;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.UniversiteRepository;
import tn.esprit.tpfoyer.service.UniversiteServiceImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class UniversiteServiceImplTest {
    @Autowired
    UniversiteServiceImpl universiteService;

    @Autowired
    UniversiteRepository universiteRepository;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        universiteRepository.deleteAll();
    }

    @Test
    @Order(1)
    @Transactional
    @Rollback
    void testAddUniversite() {
        Universite universite = new Universite(0L, "Esprit", "Some Address", new Foyer());
        Universite savedUniversite = universiteService.addUniversite(universite);
        assertNotNull(savedUniversite);
        assertTrue(savedUniversite.getIdUniversite() > 0);
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback
    void testRetrieveAllUniversites() {
        testAddUniversite(); // Ensure at least one Universite exists
        List<Universite> universites = universiteService.retrieveAllUniversites();
        assertNotNull(universites);
        assertFalse(universites.isEmpty()); // There should be at least one universite
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback
    void testRetrieveUniversiteById() {
        Universite universite = new Universite(0L, "ENIS", "Another Address", new Foyer());
        Universite savedUniversite = universiteService.addUniversite(universite);

        Universite retrievedUniversite = universiteService.retrieveUniversite(savedUniversite.getIdUniversite());
        assertNotNull(retrievedUniversite);
        assertEquals("ENIS", retrievedUniversite.getNomUniversite());
    }

    @Test
    @Order(4)
    @Transactional
    @Rollback
    void testModifyUniversite() {
        Universite universite = new Universite(0L, "ENIT", "Some Address", new Foyer());
        Universite savedUniversite = universiteService.addUniversite(universite);
        savedUniversite.setNomUniversite("ENIT Modified");

        Universite updatedUniversite = universiteService.modifyUniversite(savedUniversite);
        assertEquals("ENIT Modified", updatedUniversite.getNomUniversite());
    }

    @Test
    @Order(5)
    @Transactional
    @Rollback
    void testRemoveUniversite() {
        Universite universite = new Universite(0L, "IHEC", "Some Address", new Foyer());
        Universite savedUniversite = universiteService.addUniversite(universite);

        universiteService.removeUniversite(savedUniversite.getIdUniversite());
        assertFalse(universiteRepository.existsById(savedUniversite.getIdUniversite()));
    }
}

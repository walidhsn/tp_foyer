package tn.esprit.tpfoyer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class FoyerServiceImplTest {


    @Autowired
    FoyerServiceImpl foyerService;

    @Autowired
    FoyerRepository foyerRepository;
    @BeforeEach
    void setUp() {
        // Clear the database before each test
        foyerRepository.deleteAll();
    }
    @Test
    @Order(1)
    @Transactional
    @Rollback
    void testAddFoyer() {
        // Initialize the Foyer with an empty set of blocs
        Foyer foyer = new Foyer(0L, "Foyer A", 100L, null, new HashSet<>());
        Foyer savedFoyer = foyerService.addFoyer(foyer);
        assertNotNull(savedFoyer);
        assertTrue(savedFoyer.getIdFoyer() > 0);
    }

    @Test
    @Order(2)
    void testRetrieveAllFoyers() {
        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        assertNotNull(foyers);
        assertTrue(foyers.isEmpty() || foyers.size() > 1);
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback
    void testRetrieveFoyerById() {
        Foyer foyer = new Foyer(0L, "Foyer B", 150L, null, new HashSet<>());
        Foyer savedFoyer = foyerService.addFoyer(foyer);

        Foyer retrievedFoyer = foyerService.retrieveFoyer(savedFoyer.getIdFoyer());
        assertNotNull(retrievedFoyer);
        assertEquals("Foyer B", retrievedFoyer.getNomFoyer());
    }

    @Test
    @Order(4)
    @Transactional
    @Rollback
    void testModifyFoyer() {
        Foyer foyer = new Foyer(0L, "Foyer C", 200L, null, new HashSet<>());
        Foyer savedFoyer = foyerService.addFoyer(foyer);
        savedFoyer.setCapaciteFoyer(300L);

        Foyer updatedFoyer = foyerService.modifyFoyer(savedFoyer);
        assertEquals(300L, updatedFoyer.getCapaciteFoyer());
    }

    @Test
    @Order(5)
    @Transactional
    @Rollback
    void testRemoveFoyer() {
        Foyer foyer = new Foyer(0L, "Foyer D", 250L, null, new HashSet<>());
        Foyer savedFoyer = foyerService.addFoyer(foyer);

        foyerService.removeFoyer(savedFoyer.getIdFoyer());
        assertFalse(foyerRepository.existsById(savedFoyer.getIdFoyer()));
    }
}

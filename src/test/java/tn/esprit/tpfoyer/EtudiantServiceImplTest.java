package tn.esprit.tpfoyer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class EtudiantServiceImplTest {
    @Autowired
    EtudiantServiceImpl etudiantService;

    @Autowired
    EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        etudiantRepository.deleteAll();
    }

    @Test
    @Order(1)
    @Transactional
    @Rollback
    void testAddEtudiant() {
        Etudiant etudiant = new Etudiant(0L, "John", "Doe", 123456789L, null, null);
        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);
        assertNotNull(savedEtudiant);
        assertTrue(savedEtudiant.getIdEtudiant() > 0);
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback
    void testRetrieveAllEtudiants() {
        testAddEtudiant(); // Ensure at least one Etudiant exists
        List<Etudiant> etudiants = etudiantService.retrieveAllEtudiants();
        assertNotNull(etudiants);
        assertFalse(etudiants.isEmpty()); // There should be at least one etudiant
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback
    void testRetrieveEtudiantById() {
        Etudiant etudiant = new Etudiant(0L, "Jane", "Doe", 987654321L, null, null);
        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);

        Etudiant retrievedEtudiant = etudiantService.retrieveEtudiant(savedEtudiant.getIdEtudiant());
        assertNotNull(retrievedEtudiant);
        assertEquals("Jane", retrievedEtudiant.getNomEtudiant());
    }

    @Test
    @Order(4)
    @Transactional
    @Rollback
    void testModifyEtudiant() {
        Etudiant etudiant = new Etudiant(0L, "Alice", "Smith", 123456789L, null, null);
        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);
        savedEtudiant.setPrenomEtudiant("Johnson");

        Etudiant updatedEtudiant = etudiantService.modifyEtudiant(savedEtudiant);
        assertEquals("Johnson", updatedEtudiant.getPrenomEtudiant());
    }

    @Test
    @Order(5)
    @Transactional
    @Rollback
    void testRemoveEtudiant() {
        Etudiant etudiant = new Etudiant(0L, "Bob", "Brown", 456789123L, null, null);
        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);

        etudiantService.removeEtudiant(savedEtudiant.getIdEtudiant());
        assertFalse(etudiantRepository.existsById(savedEtudiant.getIdEtudiant()));
    }

    @Test
    @Order(6)
    @Transactional
    @Rollback
    void testRecupererEtudiantParCin() {
        Etudiant etudiant = new Etudiant(0L, "Charlie", "Green", 111222333L, null, null);
        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);

        Etudiant retrievedEtudiant = etudiantService.recupererEtudiantParCin(111222333L);
        assertNotNull(retrievedEtudiant);
        assertEquals("Charlie", retrievedEtudiant.getNomEtudiant());
    }
}

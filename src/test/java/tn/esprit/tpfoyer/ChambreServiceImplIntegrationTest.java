package tn.esprit.tpfoyer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.HashSet;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class ChambreServiceImplIntegrationTest {
    @Autowired
    private ChambreServiceImpl chambreService;

    @Autowired
    private ChambreRepository chambreRepository;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        // Initialize a Chambre before each test
        chambre = new Chambre(0L, 101, TypeChambre.SIMPLE, new HashSet<>(), null);
    }

    @Test
    void testAddChambre() {
        Chambre savedChambre = chambreService.addChambre(chambre);
        assertThat(savedChambre).isNotNull();
        assertThat(savedChambre.getIdChambre()).isGreaterThan(0);
    }

    @Test
    void testRetrieveAllChambres() {
        chambreService.addChambre(chambre); // Add Chambre to the database
        List<Chambre> listChambres = chambreService.retrieveAllChambres();
        assertThat(listChambres).isNotEmpty();
    }

    @Test
    void testRetrieveChambreById() {
        Chambre savedChambre = chambreService.addChambre(chambre);
        Chambre foundChambre = chambreService.retrieveChambre(savedChambre.getIdChambre());
        assertThat(foundChambre).isNotNull();
        assertThat(foundChambre.getIdChambre()).isEqualTo(savedChambre.getIdChambre());
    }

    @Test
    void testModifyChambre() {
        Chambre savedChambre = chambreService.addChambre(chambre);
        savedChambre.setNumeroChambre(202);
        Chambre updatedChambre = chambreService.modifyChambre(savedChambre);
        assertThat(updatedChambre.getNumeroChambre()).isEqualTo(202);
    }

    @Test
    void testRemoveChambre() {
        Chambre savedChambre = chambreService.addChambre(chambre);
        chambreService.removeChambre(savedChambre.getIdChambre());
        assertThat(chambreRepository.findById(savedChambre.getIdChambre())).isNotPresent();
    }

    @Test
    void testRecupererChambresSelonTyp() {
        chambreService.addChambre(chambre); // Add Chambre to the database
        List<Chambre> result = chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE);
        assertThat(result).isNotEmpty();
    }
}

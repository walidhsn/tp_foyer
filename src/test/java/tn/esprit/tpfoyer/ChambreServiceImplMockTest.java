package tn.esprit.tpfoyer;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class ChambreServiceImplMockTest {
    @Mock
    ChambreRepository chambreRepository;

    @InjectMocks
    ChambreServiceImpl chambreService;

    Chambre chambre;

    @BeforeEach
    void setUp() {
        // Initialize the Chambre with an empty set of reservations
        chambre = new Chambre(1L, 101, TypeChambre.SIMPLE, new HashSet<>(), null);
    }

    @Test
    @Order(1)
    void testAddChambre() {
        when(chambreRepository.save(chambre)).thenReturn(chambre);
        Chambre savedChambre = chambreService.addChambre(chambre);
        assertNotNull(savedChambre);
        assertEquals(chambre.getIdChambre(), savedChambre.getIdChambre());
    }

    @Test
    @Order(2)
    void testRetrieveAllChambres() {
        List<Chambre> chambres = new ArrayList<>();
        chambres.add(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.retrieveAllChambres();
        assertEquals(1, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    @Order(3)
    void testRetrieveChambreById() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre retrievedChambre = chambreService.retrieveChambre(1L);
        assertNotNull(retrievedChambre);
        assertEquals(1L, retrievedChambre.getIdChambre());
    }

    @Test
    @Order(4)
    void testModifyChambre() {
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre updatedChambre = chambreService.modifyChambre(chambre);
        assertEquals(chambre.getNumeroChambre(), updatedChambre.getNumeroChambre());
    }

    @Test
    @Order(5)
    void testRemoveChambre() {
        doNothing().when(chambreRepository).deleteById(1L);
        chambreService.removeChambre(1L);
        verify(chambreRepository, times(1)).deleteById(1L);
    }

    @Test
    @Order(6)
    void testRecupererChambresSelonTyp() {
        List<Chambre> chambres = new ArrayList<>();
        chambres.add(chambre);
        when(chambreRepository.findAllByTypeC(TypeChambre.SIMPLE)).thenReturn(chambres);

        List<Chambre> result = chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE);
        assertEquals(1, result.size());
        verify(chambreRepository, times(1)).findAllByTypeC(TypeChambre.SIMPLE);
    }

    @Test
    @Order(7)
    void testTrouverChambreSelonEtudiant() {
        long cin = 123456; // Example CIN
        when(chambreRepository.trouverChselonEt(cin)).thenReturn(chambre);

        Chambre foundChambre = chambreService.trouverchambreSelonEtudiant(cin);
        assertNotNull(foundChambre);
        assertEquals(chambre.getIdChambre(), foundChambre.getIdChambre());
    }
}

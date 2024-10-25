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
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class FoyerServiceImplMockTest {
    @Mock
    FoyerRepository foyerRepository;

    @InjectMocks
    FoyerServiceImpl foyerService;

    Foyer foyer;

    @BeforeEach
    void setUp() {
        // Initialize Foyer with an empty set of blocs
        foyer = new Foyer(1L, "Foyer A", 100L, null, new HashSet<>());
    }

    @Test
    @Order(1)
    void testAddFoyer() {
        when(foyerRepository.save(foyer)).thenReturn(foyer);
        Foyer savedFoyer = foyerService.addFoyer(foyer);
        assertNotNull(savedFoyer);
        assertEquals(foyer.getIdFoyer(), savedFoyer.getIdFoyer());
    }

    @Test
    @Order(2)
    void testRetrieveAllFoyers() {
        List<Foyer> foyers = new ArrayList<>();
        foyers.add(foyer);
        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.retrieveAllFoyers();
        assertEquals(1, result.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    @Order(3)
    void testRetrieveFoyerById() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer retrievedFoyer = foyerService.retrieveFoyer(1L);
        assertNotNull(retrievedFoyer);
        assertEquals(1L, retrievedFoyer.getIdFoyer());
    }

    @Test
    @Order(4)
    void testModifyFoyer() {
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer updatedFoyer = foyerService.modifyFoyer(foyer);
        assertEquals(foyer.getNomFoyer(), updatedFoyer.getNomFoyer());
    }

    @Test
    @Order(5)
    void testRemoveFoyer() {
        doNothing().when(foyerRepository).deleteById(1L);
        foyerService.removeFoyer(1L);
        verify(foyerRepository, times(1)).deleteById(1L);
    }
}

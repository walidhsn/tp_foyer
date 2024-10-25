package tn.esprit.tpfoyer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.UniversiteRepository;
import tn.esprit.tpfoyer.service.UniversiteServiceImpl;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class UniversiteServiceImplMockTest {
    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    UniversiteServiceImpl universiteService;

    Universite universite;

    @BeforeEach
    void setUp() {
        universite = new Universite(1L, "Université Esprit", "Tunis, Tunisia", null);
    }

    @Test
    @Order(1)
    void testAddUniversite() {
        when(universiteRepository.save(universite)).thenReturn(universite);
        Universite savedUniversite = universiteService.addUniversite(universite);
        assertNotNull(savedUniversite);
        assertEquals(universite.getIdUniversite(), savedUniversite.getIdUniversite());
    }

    @Test
    @Order(2)
    void testRetrieveAllUniversites() {
        List<Universite> universites = new ArrayList<>();
        universites.add(universite);
        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.retrieveAllUniversites();
        assertEquals(1, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    @Order(3)
    void testRetrieveUniversiteById() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite retrievedUniversite = universiteService.retrieveUniversite(1L);
        assertNotNull(retrievedUniversite);
        assertEquals(1L, retrievedUniversite.getIdUniversite());
    }

    @Test
    @Order(4)
    void testModifyUniversite() {
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite updatedUniversite = universiteService.modifyUniversite(universite);
        assertEquals(universite.getIdUniversite(), updatedUniversite.getIdUniversite());
    }

    @Test
    @Order(5)
    void testRemoveUniversite() {
        doNothing().when(universiteRepository).deleteById(1L);
        universiteService.removeUniversite(1L);
        verify(universiteRepository, times(1)).deleteById(1L);
    }
}

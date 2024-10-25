package tn.esprit.tpfoyer;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class EtudiantServiceImplMockTest {
    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    public void testRetrieveAllEtudiants() {
        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(new Etudiant(1, "John", "Doe", 123456, new Date(),new HashSet<>())));

        assertEquals(1, etudiantService.retrieveAllEtudiants().size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    @Order(2)
    public void testRetrieveEtudiant() {
        Etudiant etudiant = new Etudiant(1, "John", "Doe", 123456, new Date(),new HashSet<>());
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.retrieveEtudiant(1L);
        assertEquals("John", result.getNomEtudiant());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    @Order(3)
    public void testAddEtudiant() {
        Etudiant etudiant = new Etudiant(1, "John", "Doe", 123456, new Date(),new HashSet<>());
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);
        assertEquals("John", result.getNomEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    @Order(4)
    public void testModifyEtudiant() {
        Etudiant etudiant = new Etudiant(1, "John", "Doe", 123456, new Date(),new HashSet<>());
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.modifyEtudiant(etudiant);
        assertEquals("John", result.getNomEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    @Order(5)
    public void testRemoveEtudiant() {
        Long id = 1L;
        doNothing().when(etudiantRepository).deleteById(id);

        etudiantService.removeEtudiant(id);
        verify(etudiantRepository, times(1)).deleteById(id);
    }
}

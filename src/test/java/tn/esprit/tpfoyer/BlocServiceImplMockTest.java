package tn.esprit.tpfoyer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class BlocServiceImplMockTest {
    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    BlocServiceImpl blocService;

    Bloc bloc;

    @BeforeEach
    void setUp() {
        bloc = new Bloc(1L, "Bloc A", 100L, null, new HashSet<>());
    }

    @Test
    @Order(1)
    void testAddBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);
        Bloc savedBloc = blocService.addBloc(bloc);
        assertNotNull(savedBloc);
        assertEquals(bloc.getIdBloc(), savedBloc.getIdBloc());
    }

    @Test
    @Order(2)
    void testRetrieveAllBlocs() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);
        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.retrieveAllBlocs();
        assertEquals(1, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    @Order(3)
    void testRetrieveBlocById() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc retrievedBloc = blocService.retrieveBloc(1L);
        assertNotNull(retrievedBloc);
        assertEquals(1L, retrievedBloc.getIdBloc());
    }

    @Test
    @Order(4)
    void testModifyBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc updatedBloc = blocService.modifyBloc(bloc);
        assertEquals(bloc.getNomBloc(), updatedBloc.getNomBloc());
    }

    @Test
    @Order(5)
    @Rollback
    @Transactional
    void testRemoveBloc() {
        doNothing().when(blocRepository).deleteById(1L);
        blocService.removeBloc(1L);
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    @Order(6)
    void testRetrieveBlocsSelonCapacite() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);
        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(50L);
        assertEquals(1, result.size());
    }

    @Test
    @Order(7)
    void testFindBlocsWithoutFoyer() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(blocs);

        List<Bloc> result = blocService.trouverBlocsSansFoyer();
        assertEquals(1, result.size());
    }

    @Test
    @Order(8)
    void testFindBlocsByNameAndCapacity() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc A", 100L)).thenReturn(blocs);

        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Bloc A", 100L);
        assertEquals(1, result.size());
    }
}

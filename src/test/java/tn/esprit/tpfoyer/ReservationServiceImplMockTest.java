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
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplMockTest {
    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationServiceImpl reservationService;

    Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation("1", new Date(), true, null);
    }

    @Test
    @Order(1)
    void testAddReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        Reservation savedReservation = reservationService.addReservation(reservation);
        assertNotNull(savedReservation);
        assertEquals(reservation.getIdReservation(), savedReservation.getIdReservation());
    }

    @Test
    @Order(2)
    void testRetrieveAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.retrieveAllReservations();
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    @Order(3)
    void testRetrieveReservationById() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation retrievedReservation = reservationService.retrieveReservation("1");
        assertNotNull(retrievedReservation);
        assertEquals("1", retrievedReservation.getIdReservation());
    }

    @Test
    @Order(4)
    void testModifyReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation updatedReservation = reservationService.modifyReservation(reservation);
        assertEquals(reservation.getIdReservation(), updatedReservation.getIdReservation());
    }

    @Test
    @Order(5)
    void testRemoveReservation() {
        doNothing().when(reservationRepository).deleteById("1");
        reservationService.removeReservation("1");
        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    @Order(6)
    void testFindReservationsByDateAndStatus() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), any(Boolean.class)))
                .thenReturn(reservations);

        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), true);
        assertEquals(1, result.size());
    }
}

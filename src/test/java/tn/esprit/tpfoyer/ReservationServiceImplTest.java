package tn.esprit.tpfoyer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class ReservationServiceImplTest {
    @Autowired
    ReservationServiceImpl reservationService;

    @Autowired
    ReservationRepository reservationRepository;
    void setUp() {
        // Clear the database before each test
        reservationRepository.deleteAll();
    }
    @Test
    @Order(1)
    @Transactional
    @Rollback
    void testAddReservation() {
        Reservation reservation = new Reservation("1", new Date(), true, null);
        Reservation savedReservation = reservationService.addReservation(reservation);
        assertNotNull(savedReservation);
        assertEquals("1", savedReservation.getIdReservation());
    }

    @Test
    @Order(2)
    void testRetrieveAllReservations() {
        List<Reservation> reservations = reservationService.retrieveAllReservations();
        assertNotNull(reservations);
        assertTrue(!reservations.isEmpty());
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback
    void testRetrieveReservationById() {
        Reservation reservation = new Reservation("2", new Date(), true, null);
        reservationService.addReservation(reservation);
        Reservation retrievedReservation = reservationService.retrieveReservation("2");
        assertNotNull(retrievedReservation);
        assertEquals("2", retrievedReservation.getIdReservation());
    }

    @Test
    @Order(4)
    @Transactional
    @Rollback
    void testModifyReservation() {
        Reservation reservation = new Reservation("3", new Date(), true, null);
        reservationService.addReservation(reservation);
        reservation.setEstValide(false);
        Reservation updatedReservation = reservationService.modifyReservation(reservation);
        assertFalse(updatedReservation.isEstValide());
    }

    @Test
    @Order(5)
    @Transactional
    @Rollback
    void testRemoveReservation() {
        Reservation reservation = new Reservation("4", new Date(), true, null);
        reservationService.addReservation(reservation);
        reservationService.removeReservation("4");
        assertFalse(reservationRepository.existsById("4"));
    }
}

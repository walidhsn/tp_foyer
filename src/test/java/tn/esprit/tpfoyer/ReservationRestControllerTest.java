package tn.esprit.tpfoyer;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(ReservationRestController.class)
public class ReservationRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReservationService reservationService;

    @Test
    void testGetReservations() throws Exception {
        List<Reservation> mockReservations = Arrays.asList(
                new Reservation("1", new Date(), true, null),
                new Reservation("2", new Date(), false, null)
        );
        when(reservationService.retrieveAllReservations()).thenReturn(mockReservations);

        mockMvc.perform(get("/reservation/retrieve-all-reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idReservation", is("1")))
                .andExpect(jsonPath("$[1].idReservation", is("2")));
    }

    @Test
    void testAddReservation() throws Exception {
        Reservation mockReservation = new Reservation("3", new Date(), true, null);
        when(reservationService.addReservation(any(Reservation.class))).thenReturn(mockReservation);

        mockMvc.perform(post("/reservation/add-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idReservation\": \"3\", \"anneeUniversitaire\": \"2024-09-01\", \"estValide\": true }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idReservation", is("3")));
    }

    @Test
    void testModifyReservation() throws Exception {
        Reservation mockReservation = new Reservation("4", new Date(), false, null);
        when(reservationService.modifyReservation(any(Reservation.class))).thenReturn(mockReservation);

        mockMvc.perform(put("/reservation/modify-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idReservation\": \"4\", \"anneeUniversitaire\": \"2024-09-01\", \"estValide\": false }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idReservation", is("4")));
    }

    @Test
    void testRemoveReservation() throws Exception {
        mockMvc.perform(delete("/reservation/remove-reservation/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservationById() throws Exception {
        Reservation mockReservation = new Reservation("5", new Date(), true, null);
        when(reservationService.retrieveReservation("5")).thenReturn(mockReservation);

        mockMvc.perform(get("/reservation/retrieve-reservation/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idReservation", is("5")));
    }
}

package tn.esprit.tpfoyer;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer.control.ChambreRestController;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.service.IChambreService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@WebMvcTest(ChambreRestController.class)
public class ChambreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChambreService chambreService;

    @Test
    void testGetChambres() throws Exception {
        // Mocking the service layer
        List<Chambre> mockChambres = Arrays.asList(
                new Chambre(1L, 101L, TypeChambre.SIMPLE, new HashSet<>(), new Bloc()),
                new Chambre(2L, 102L, TypeChambre.DOUBLE, new HashSet<>(), new Bloc())
        );
        when(chambreService.retrieveAllChambres()).thenReturn(mockChambres);

        // Perform GET request and verify response
        mockMvc.perform(get("/chambre/retrieve-all-chambres"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroChambre", is(101)))
                .andExpect(jsonPath("$[0].typeC", is("SIMPLE")))
                .andExpect(jsonPath("$[1].numeroChambre", is(102)))
                .andExpect(jsonPath("$[1].typeC", is("DOUBLE")));
    }

    @Test
    void testRetrieveChambre() throws Exception {
        // Mocking the service layer
        Chambre mockChambre = new Chambre(1L, 101L, TypeChambre.SIMPLE, new HashSet<>(), new Bloc());
        when(chambreService.retrieveChambre(1L)).thenReturn(mockChambre);

        // Perform GET request and verify response
        mockMvc.perform(get("/chambre/retrieve-chambre/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroChambre", is(101)))
                .andExpect(jsonPath("$.typeC", is("SIMPLE")));
    }

    @Test
    void testAddChambre() throws Exception {
        // Mocking the service layer
        Chambre mockChambre = new Chambre(3L, 103L, TypeChambre.TRIPLE, new HashSet<>(), new Bloc());
        when(chambreService.addChambre(any(Chambre.class))).thenReturn(mockChambre);

        // Perform POST request and verify response
        mockMvc.perform(post("/chambre/add-chambre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"numeroChambre\": 103, \"typeC\": \"TRIPLE\" }"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroChambre", is(103)))
                .andExpect(jsonPath("$.typeC", is("TRIPLE")));
    }

    @Test
    void testRemoveChambre() throws Exception {
        // Perform DELETE request
        mockMvc.perform(delete("/chambre/remove-chambre/1"))
                .andDo(print())
                .andExpect(status().isOk());

        // Verify that the service method was called
        verify(chambreService, times(1)).removeChambre(1L);
    }

    @Test
    void testModifyChambre() throws Exception {
        // Mocking the service layer
        Chambre mockChambre = new Chambre(1L, 104L, TypeChambre.DOUBLE, new HashSet<>(), new Bloc());
        when(chambreService.modifyChambre(any(Chambre.class))).thenReturn(mockChambre);

        // Perform PUT request and verify response
        mockMvc.perform(put("/chambre/modify-chambre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idChambre\": 1, \"numeroChambre\": 104, \"typeC\": \"DOUBLE\" }"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroChambre", is(104)))
                .andExpect(jsonPath("$.typeC", is("DOUBLE")));
    }

    @Test
    void testTrouverChambresSelonTC() throws Exception {
        // Mocking the service layer
        List<Chambre> mockChambres = Arrays.asList(
                new Chambre(1L, 105L, TypeChambre.SIMPLE, new HashSet<>(), new Bloc())
        );
        when(chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE)).thenReturn(mockChambres);

        // Perform GET request and verify response
        mockMvc.perform(get("/chambre/trouver-chambres-selon-typ/SIMPLE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].numeroChambre", is(105)))
                .andExpect(jsonPath("$[0].typeC", is("SIMPLE")));
    }
}

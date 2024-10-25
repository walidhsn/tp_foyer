package tn.esprit.tpfoyer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.FoyerRestController;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.service.IFoyerService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoyerRestController.class)
public class FoyerRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFoyerService foyerService;

    @Test
    void testGetFoyers() throws Exception {
        List<Foyer> mockFoyers = Arrays.asList(
                new Foyer(1L, "Foyer A", 1L,null,new HashSet<>()),
                new Foyer(2L, "Foyer B", 1L,null,new HashSet<>())
        );
        when(foyerService.retrieveAllFoyers()).thenReturn(mockFoyers);

        mockMvc.perform(get("/foyer/retrieve-all-foyers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nomFoyer", is("Foyer A")))
                .andExpect(jsonPath("$[1].nomFoyer", is("Foyer B")));
    }

    @Test
    void testAddFoyer() throws Exception {
        Foyer mockFoyer = new Foyer(1L, "Foyer C", 1L,null,new HashSet<>());
        when(foyerService.addFoyer(any(Foyer.class))).thenReturn(mockFoyer);

        mockMvc.perform(post("/foyer/add-foyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nomFoyer\": \"Foyer C\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomFoyer", is("Foyer C")));
    }

    @Test
    void testModifyFoyer() throws Exception {
        Foyer mockFoyer = new Foyer(1L, "Foyer D", 1L,null,new HashSet<>());
        when(foyerService.modifyFoyer(any(Foyer.class))).thenReturn(mockFoyer);

        mockMvc.perform(put("/foyer/modify-foyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idFoyer\": 1, \"nomFoyer\": \"Foyer D\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomFoyer", is("Foyer D")));
    }

    @Test
    void testRemoveFoyer() throws Exception {
        mockMvc.perform(delete("/foyer/remove-foyer/1"))
                .andExpect(status().isOk());
    }
}

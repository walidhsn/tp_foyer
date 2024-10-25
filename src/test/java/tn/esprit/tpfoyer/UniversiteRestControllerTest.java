package tn.esprit.tpfoyer;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.UniversiteRestController;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.service.IUniversiteService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UniversiteRestController.class)
public class UniversiteRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUniversiteService universiteService;

    private Universite universite1;
    private Universite universite2;
    private Foyer foyer1;

    @BeforeEach
    void setUp() {
        // Mock data setup with Foyer
        foyer1 = new Foyer(); // Assuming Foyer class has a no-argument constructor
        foyer1.setIdFoyer(1L);
        foyer1.setNomFoyer("Foyer Name"); // Sample data for Foyer

        universite1 = new Universite(1L, "Esprit", "Esprit Address", foyer1);
        universite2 = new Universite(2L, "ENIS", "ENIS Address", null); // No associated Foyer
    }

    @Test
    void testGetUniversites() throws Exception {
        List<Universite> mockUniversites = Arrays.asList(universite1, universite2);
        when(universiteService.retrieveAllUniversites()).thenReturn(mockUniversites);

        mockMvc.perform(get("/universite/retrieve-all-universites")) // No base path needed here
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idUniversite", is(1)))
                .andExpect(jsonPath("$[0].nomUniversite", is("Esprit")))
                .andExpect(jsonPath("$[0].foyer.nomFoyer", is("Foyer Name")))
                .andExpect(jsonPath("$[1].idUniversite", is(2)))
                .andExpect(jsonPath("$[1].nomUniversite", is("ENIS")))
                .andExpect(jsonPath("$[1].foyer").doesNotExist()); // Check for null foyer
    }

    @Test
    void testAddUniversite() throws Exception {
        Universite mockUniversite = new Universite(3L, "IHEC", "IHEC Address", null);
        when(universiteService.addUniversite(any(Universite.class))).thenReturn(mockUniversite);

        mockMvc.perform(post("/universite/add-universite") // No base path needed here
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nomUniversite\": \"IHEC\", \"adresse\": \"IHEC Address\", \"foyer\": null }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUniversite", is(3)))
                .andExpect(jsonPath("$.nomUniversite", is("IHEC")));
    }

    @Test
    void testModifyUniversite() throws Exception {
        Universite mockUniversite = new Universite(4L, "Modified Name", "Modified Address", foyer1);
        when(universiteService.modifyUniversite(any(Universite.class))).thenReturn(mockUniversite);

        mockMvc.perform(put("/universite/modify-universite") // No base path needed here
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idUniversite\": 4, \"nomUniversite\": \"Modified Name\", \"adresse\": \"Modified Address\", \"foyer\": { \"idFoyer\": 1, \"nomFoyer\": \"Foyer Name\" } }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUniversite", is(4)))
                .andExpect(jsonPath("$.nomUniversite", is("Modified Name")))
                .andExpect(jsonPath("$.foyer.nomFoyer", is("Foyer Name")));
    }

    @Test
    void testRemoveUniversite() throws Exception {
        mockMvc.perform(delete("/universite/remove-universite/1")) // No base path needed here
                .andExpect(status().isOk());
    }

    @Test
    void testGetUniversiteById() throws Exception {
        when(universiteService.retrieveUniversite(1L)).thenReturn(universite1);

        mockMvc.perform(get("/universite/retrieve-universite/1")) // No base path needed here
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUniversite", is(1)))
                .andExpect(jsonPath("$.nomUniversite", is("Esprit")))
                .andExpect(jsonPath("$.foyer.nomFoyer", is("Foyer Name")));
    }
}

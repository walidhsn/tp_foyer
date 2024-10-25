package tn.esprit.tpfoyer;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.EtudiantRestController;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@WebMvcTest(EtudiantRestController.class)
public class EtudiantRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEtudiantService etudiantService;

    private Etudiant mockEtudiant1;
    private Etudiant mockEtudiant2;

    @BeforeEach
    void setUp() {
        // Initialize common mock Etudiant objects
        mockEtudiant1 = new Etudiant(1L, "John", "Doe", 123456, null, new HashSet<>());
        mockEtudiant2 = new Etudiant(2L, "Jane", "Smith", 654321, null, new HashSet<>());
    }

    @Test
    void testGetEtudiants() throws Exception {
        // Mocking the service layer to return a list of Etudiants
        List<Etudiant> mockEtudiants = Arrays.asList(mockEtudiant1, mockEtudiant2);
        when(etudiantService.retrieveAllEtudiants()).thenReturn(mockEtudiants);

        // Perform GET request and verify response
        mockMvc.perform(get("/etudiant/retrieve-all-etudiants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nomEtudiant", is("John")))
                .andExpect(jsonPath("$[1].nomEtudiant", is("Jane")));
    }

    @Test
    void testRetrieveEtudiant() throws Exception {
        // Mocking the service layer to return a specific Etudiant
        when(etudiantService.retrieveEtudiant(1L)).thenReturn(mockEtudiant1);

        // Perform GET request and verify response
        mockMvc.perform(get("/etudiant/retrieve-etudiant/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomEtudiant", is("John")))
                .andExpect(jsonPath("$.cinEtudiant", is(123456)));
    }

    @Test
    void testAddEtudiant() throws Exception {
        // Mocking the service layer to return a newly added Etudiant
        Etudiant mockNewEtudiant = new Etudiant(3L, "Alice", "Johnson", 789012, null, new HashSet<>());
        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(mockNewEtudiant);

        // Perform POST request and verify response
        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nomEtudiant\": \"Alice\", \"prenomEtudiant\": \"Johnson\", \"cinEtudiant\": 789012 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomEtudiant", is("Alice")))
                .andExpect(jsonPath("$.cinEtudiant", is(789012)));
    }

    @Test
    void testRemoveEtudiant() throws Exception {
        // Perform DELETE request
        mockMvc.perform(delete("/etudiant/remove-etudiant/1"))
                .andExpect(status().isOk());

        // Verify that the service method was called
        verify(etudiantService, times(1)).removeEtudiant(1L);
    }

    @Test
    void testModifyEtudiant() throws Exception {
        // Mocking the service layer to return a modified Etudiant
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenReturn(mockEtudiant1);

        // Perform PUT request and verify response
        mockMvc.perform(put("/etudiant/modify-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idEtudiant\": 1, \"nomEtudiant\": \"John\", \"prenomEtudiant\": \"Doe\", \"cinEtudiant\": 654321 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cinEtudiant", is(123456))); // Adjust based on your requirements
    }

    @Test
    void testRetrieveEtudiantParCin() throws Exception {
        // Mocking the service layer to return an Etudiant by CIN
        when(etudiantService.recupererEtudiantParCin(123456)).thenReturn(mockEtudiant1);

        // Perform GET request and verify response
        mockMvc.perform(get("/etudiant/retrieve-etudiant-cin/123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomEtudiant", is("John")))
                .andExpect(jsonPath("$.cinEtudiant", is(123456)));
    }
}

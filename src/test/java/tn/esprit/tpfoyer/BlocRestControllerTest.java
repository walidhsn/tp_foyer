package tn.esprit.tpfoyer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.BlocRestController;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.service.IBlocService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
public class BlocRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlocService blocService;

    @Test
    void testGetBlocs() throws Exception {
        List<Bloc> mockBlocs = Arrays.asList(
                new Bloc(1L, "Bloc A", 10, null,new HashSet<>()),
                new Bloc(2L, "Bloc B", 20, null,new HashSet<>())
        );
        when(blocService.retrieveAllBlocs()).thenReturn(mockBlocs);

        mockMvc.perform(get("/bloc/retrieve-all-blocs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nomBloc", is("Bloc A")))
                .andExpect(jsonPath("$[1].nomBloc", is("Bloc B")));
    }

    @Test
    void testAddBloc() throws Exception {
        Bloc mockBloc = new Bloc(1L, "Bloc C", 30, null,new HashSet<>());
        when(blocService.addBloc(any(Bloc.class))).thenReturn(mockBloc);

        mockMvc.perform(post("/bloc/add-bloc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nomBloc\": \"Bloc C\", \"capaciteBloc\": 30 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomBloc", is("Bloc C")))
                .andExpect(jsonPath("$.capaciteBloc", is(30)));
    }

    @Test
    void testModifyBloc() throws Exception {
        Bloc mockBloc = new Bloc(1L, "Bloc D", 40, null,new HashSet<>());
        when(blocService.modifyBloc(any(Bloc.class))).thenReturn(mockBloc);

        mockMvc.perform(put("/bloc/modify-bloc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idBloc\": 1, \"nomBloc\": \"Bloc D\", \"capaciteBloc\": 40 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomBloc", is("Bloc D")))
                .andExpect(jsonPath("$.capaciteBloc", is(40)));
    }

    @Test
    void testRemoveBloc() throws Exception {
        mockMvc.perform(delete("/bloc/remove-bloc/1"))
                .andExpect(status().isOk());
    }
}

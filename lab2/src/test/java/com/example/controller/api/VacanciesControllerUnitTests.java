package com.example.controller.api;

import com.example.dto.VacancyCreateDTO;
import com.example.dto.VacancyDTO;
import com.example.dto.VacancyUpdateDTO;
import com.example.service.VacancyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VacanciesController.class)
@Import(JsonNullableModule.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VacanciesControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VacancyService vacancyService;

    @Autowired
    private ObjectMapper objectMapper;

    private VacancyDTO vacancyDTO;

    @BeforeEach
    public void setup() {

        objectMapper.registerModule(new JsonNullableModule());

        vacancyDTO = new VacancyDTO();
        vacancyDTO.setId(1L);
        vacancyDTO.setTitle("Java Developer");
        vacancyDTO.setLink("https://example.com/vacancies/java-dev");
        vacancyDTO.setBody("We are looking for a Middle/Senior Java Developer.");
    }

    @Test
    @Order(1)
    public void createVacancyTest() throws Exception {

        VacancyCreateDTO createDTO = new VacancyCreateDTO();
        createDTO.setTitle("Java Developer");
        createDTO.setLink("https://example.com/vacancies/java-dev");
        createDTO.setBody("We are looking for a Middle/Senior Java Developer.");
        createDTO.setCompanyId(1L);

        given(vacancyService.create(any(VacancyCreateDTO.class))).willReturn(vacancyDTO);

        ResultActions response = mockMvc.perform(post("/api/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(vacancyDTO.getTitle())))
                .andExpect(jsonPath("$.link", is(vacancyDTO.getLink())))
                .andExpect(jsonPath("$.body", is(vacancyDTO.getBody())));
    }

    @Test
    @Order(2)
    public void getAllVacanciesTest() throws Exception {

        VacancyDTO secondVacancy = new VacancyDTO();
        secondVacancy.setId(2L);
        secondVacancy.setTitle("Kotlin Developer");
        secondVacancy.setLink("https://example.com/vacancies/kotlin-dev");
        secondVacancy.setBody("Kotlin vacancy body");

        List<VacancyDTO> vacanciesList = new ArrayList<>();
        vacanciesList.add(vacancyDTO);
        vacanciesList.add(secondVacancy);

        given(vacancyService.getAll()).willReturn(vacanciesList);

        ResultActions response = mockMvc.perform(get("/api/vacancies"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(vacanciesList.size())))
                .andExpect(jsonPath("$[0].title", is("Java Developer")))
                .andExpect(jsonPath("$[1].title", is("Kotlin Developer")));
    }

    @Test
    @Order(3)
    public void getVacancyByIdTest() throws Exception {

        given(vacancyService.findById(vacancyDTO.getId())).willReturn(vacancyDTO);

        ResultActions response = mockMvc.perform(get("/api/vacancies/{id}", vacancyDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(vacancyDTO.getTitle())))
                .andExpect(jsonPath("$.link", is(vacancyDTO.getLink())))
                .andExpect(jsonPath("$.body", is(vacancyDTO.getBody())));
    }

    @Test
    @Order(4)
    public void updateVacancyTest() throws Exception {

        VacancyUpdateDTO updateDTO = new VacancyUpdateDTO();
        updateDTO.setTitle(JsonNullable.of("Senior Java Developer"));
        updateDTO.setLink(JsonNullable.of("https://example.com/vacancies/java-dev"));
        updateDTO.setBody(JsonNullable.of("Updated description for Senior role."));

        VacancyDTO updatedDTO = new VacancyDTO();
        updatedDTO.setId(1L);
        updatedDTO.setTitle("Senior Java Developer");
        updatedDTO.setLink("https://example.com/vacancies/java-dev");
        updatedDTO.setBody("Updated description for Senior role.");

        given(vacancyService.update(any(VacancyUpdateDTO.class), eq(vacancyDTO.getId()))).willReturn(updatedDTO);

        ResultActions response = mockMvc.perform(put("/api/vacancies/{id}", vacancyDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Senior Java Developer")))
                .andExpect(jsonPath("$.body", is("Updated description for Senior role.")));
    }

    @Test
    @Order(5)
    public void deleteVacancyTest() throws Exception {

        willDoNothing().given(vacancyService).delete(vacancyDTO.getId());

        ResultActions response = mockMvc.perform(delete("/api/vacancies/{id}", vacancyDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }
}
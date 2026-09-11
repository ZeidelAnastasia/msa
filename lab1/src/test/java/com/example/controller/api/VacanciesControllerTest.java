package com.example.controller.api;

import com.example.dto.VacancyUpdateDTO;
import com.example.entity.Vacancy;
import com.example.entity.User;
import com.example.mapper.VacancyMapper;
import com.example.repository.VacancyRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VacanciesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VacancyMapper vacancyMapper;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private UserRepository userRepository;

    private final Faker faker = new Faker();
    private User testUser;
    private Vacancy testVacancy;

    @BeforeEach
    public void setUp() {
        vacancyRepository.deleteAll();
        userRepository.deleteAll();

        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(testUser);

        testVacancy = Instancio.of(Vacancy.class)
                .ignore(Select.field(Vacancy::getId))
                .supply(Select.field(Vacancy::getTitle), () -> faker.gameOfThrones().house())
                .supply(Select.field(Vacancy::getBody), () -> faker.gameOfThrones().quote())
                .supply(Select.field(Vacancy::getCompany), () -> testUser)
                .create();
    }

    @Test
    public void testIndex() throws Exception {
        vacancyRepository.save(testVacancy);
        var result = mockMvc.perform(get("/api/vacancies"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testCreate() throws Exception {
        var dto = vacancyMapper.map(testVacancy);
        var request = post("/api/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var vacancy = vacancyRepository.findByLink(dto.getLink()).orElse(null);
        assertNotNull(vacancy);
        assertThat(vacancy.getTitle()).isEqualTo(dto.getTitle());
    }

    @Test
    public void testUpdate() throws Exception {
        vacancyRepository.save(testVacancy);
        var dto = new VacancyUpdateDTO();
        dto.setTitle(JsonNullable.of("new title"));

        var request = put("/api/vacancies/" + testVacancy.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var vacancy = vacancyRepository.findById(testVacancy.getId()).get();
        assertThat(vacancy.getTitle()).isEqualTo(dto.getTitle().get());
    }

    @Test
    public void testShow() throws Exception {
        vacancyRepository.save(testVacancy);
        var request = get("/api/vacancies/" + testVacancy.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("link").isEqualTo(testVacancy.getLink()),
                v -> v.node("title").isEqualTo(testVacancy.getTitle()),
                v -> v.node("body").isEqualTo(testVacancy.getBody())
        );
    }

    @Test
    public void testDestroy() throws Exception {
        vacancyRepository.save(testVacancy);
        var request = delete("/api/vacancies/" + testVacancy.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(vacancyRepository.existsById(testVacancy.getId())).isFalse();
    }
}

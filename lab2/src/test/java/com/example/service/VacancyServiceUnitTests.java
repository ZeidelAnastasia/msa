package com.example.service;

import com.example.dto.VacancyCreateDTO;
import com.example.dto.VacancyDTO;
import com.example.dto.VacancyUpdateDTO;
import com.example.entity.Vacancy;
import com.example.mapper.VacancyMapper;
import com.example.repository.VacancyRepository;
import com.example.service.impl.VacancyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VacancyServiceUnitTests {

    @Mock
    private VacancyRepository repository;

    @Mock
    private VacancyMapper vacancyMapper;

    @InjectMocks
    private VacancyServiceImpl vacancyService;

    private Vacancy vacancy;
    private VacancyDTO vacancyDTO;
    private VacancyCreateDTO createDTO;
    private VacancyUpdateDTO updateDTO;

    @BeforeEach
    public void setup() {
        vacancy = new Vacancy();
        vacancy.setId(1L);
        vacancy.setTitle("Java Developer");
        vacancy.setLink("https://example.com/vacancies/java-dev");
        vacancy.setBody("We are looking for a Middle/Senior Java Developer.");

        vacancyDTO = new VacancyDTO();
        vacancyDTO.setId(1L);
        vacancyDTO.setTitle("Java Developer");
        vacancyDTO.setLink("https://example.com/vacancies/java-dev");
        vacancyDTO.setBody("We are looking for a Middle/Senior Java Developer.");

        createDTO = new VacancyCreateDTO();
        createDTO.setTitle("Java Developer");
        createDTO.setLink("https://example.com/vacancies/java-dev");
        createDTO.setBody("We are looking for a Middle/Senior Java Developer.");

        updateDTO = new VacancyUpdateDTO();
        updateDTO.setTitle(JsonNullable.of("Senior Java Developer"));
        updateDTO.setLink(JsonNullable.of("https://example.com/vacancies/java-dev"));
        updateDTO.setBody(JsonNullable.of("Updated description"));
    }

    @Test
    @Order(1)
    public void createVacancyTest() {
        given(vacancyMapper.map(createDTO)).willReturn(vacancy);
        given(repository.save(vacancy)).willReturn(vacancy);
        given(vacancyMapper.map(vacancy)).willReturn(vacancyDTO);

        VacancyDTO savedVacancy = vacancyService.create(createDTO);

        System.out.println(savedVacancy);
        assertThat(savedVacancy).isNotNull();
        assertThat(savedVacancy.getId()).isEqualTo(1L);
        assertThat(savedVacancy.getTitle()).isEqualTo("Java Developer");
    }

    @Test
    @Order(2)
    public void getVacancyByIdTest() {
        given(repository.findById(1L)).willReturn(Optional.of(vacancy));
        given(vacancyMapper.map(vacancy)).willReturn(vacancyDTO);

        VacancyDTO existingVacancy = vacancyService.findById(1L);

        System.out.println(existingVacancy);
        assertThat(existingVacancy).isNotNull();
        assertThat(existingVacancy.getId()).isEqualTo(1L);
        assertThat(existingVacancy.getTitle()).isEqualTo("Java Developer");
    }

    @Test
    @Order(3)
    public void getAllVacanciesTest() {
        Vacancy vacancy2 = new Vacancy();
        vacancy2.setId(2L);
        vacancy2.setTitle("Kotlin Developer");

        VacancyDTO vacancyDTO2 = new VacancyDTO();
        vacancyDTO2.setId(2L);
        vacancyDTO2.setTitle("Kotlin Developer");

        given(repository.findAll()).willReturn(List.of(vacancy, vacancy2));
        given(vacancyMapper.map(vacancy)).willReturn(vacancyDTO);
        given(vacancyMapper.map(vacancy2)).willReturn(vacancyDTO2);

        List<VacancyDTO> vacancyList = vacancyService.getAll();

        System.out.println(vacancyList);
        assertThat(vacancyList).isNotNull();
        assertThat(vacancyList.size()).isEqualTo(2);
    }

    @Test
    @Order(4)
    public void updateVacancyTest() {
        given(repository.findById(1L)).willReturn(Optional.of(vacancy));
        willDoNothing().given(vacancyMapper).update(updateDTO, vacancy);
        given(repository.save(vacancy)).willReturn(vacancy);
        given(vacancyMapper.map(vacancy)).willReturn(vacancyDTO);

        VacancyDTO updatedVacancy = vacancyService.update(updateDTO, 1L);

        System.out.println(updatedVacancy);
        assertThat(updatedVacancy).isNotNull();
        assertThat(updatedVacancy.getId()).isEqualTo(1L);
    }

    @Test
    @Order(5)
    public void deleteVacancyTest() {
        willDoNothing().given(repository).deleteById(1L);

        vacancyService.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
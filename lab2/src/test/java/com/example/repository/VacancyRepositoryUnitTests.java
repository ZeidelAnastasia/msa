package com.example.repository;

import com.example.entity.User;
import com.example.entity.Vacancy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class VacancyRepositoryUnitTests {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User company;
    private Vacancy vacancy;

    @BeforeEach
    void setUp() {

        company = new User();
        company.setEmail("company@example.com");
        company.setTitle("Tech Corp");
        entityManager.persist(company);

        vacancy = new Vacancy();
        vacancy.setTitle("Java Developer");
        vacancy.setLink("https://example.com/vacancies/java-dev");
        vacancy.setBody("We are looking for a Middle/Senior Java Developer.");
        vacancy.setCompany(company);
        vacancy.setCreatedAt(LocalDate.now());
        vacancy.setUpdatedAt(LocalDate.now());

        entityManager.persist(vacancy);
        entityManager.flush();
    }

    @Test
    @DisplayName("Test 1: Save Vacancy Test")
    public void saveVacancyTest() {
        Vacancy newVacancy = new Vacancy();
        newVacancy.setTitle("Kotlin Developer");
        newVacancy.setLink("https://example.com/vacancies/kotlin-dev");
        newVacancy.setBody("Kotlin Developer needed");
        newVacancy.setCompany(company);
        newVacancy.setCreatedAt(LocalDate.now());
        newVacancy.setUpdatedAt(LocalDate.now());

        Vacancy savedVacancy = vacancyRepository.save(newVacancy);

        Assertions.assertThat(savedVacancy.getId()).isNotNull();
        Assertions.assertThat(savedVacancy.getId()).isGreaterThan(0);
        Assertions.assertThat(savedVacancy.getTitle()).isEqualTo("Kotlin Developer");
    }

    @Test
    @DisplayName("Test 2: Get Vacancy By ID Test")
    public void getVacancyTest() {
        Vacancy foundVacancy = vacancyRepository.findById(vacancy.getId()).orElse(null);

        Assertions.assertThat(foundVacancy).isNotNull();
        Assertions.assertThat(foundVacancy.getId()).isEqualTo(vacancy.getId());
        Assertions.assertThat(foundVacancy.getTitle()).isEqualTo("Java Developer");
    }

    @Test
    @DisplayName("Test 3: Get List of Vacancies Test")
    public void getListOfVacanciesTest() {
        List<Vacancy> vacancies = vacancyRepository.findAll();

        Assertions.assertThat(vacancies).isNotEmpty();
        Assertions.assertThat(vacancies.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test 4: Update Vacancy Test")
    public void updateVacancyTest() {
        Vacancy foundVacancy = vacancyRepository.findById(vacancy.getId()).orElseThrow();
        foundVacancy.setTitle("Senior Java Developer");
        foundVacancy.setBody("Updated description.");

        Vacancy updatedVacancy = vacancyRepository.save(foundVacancy);

        Assertions.assertThat(updatedVacancy.getTitle()).isEqualTo("Senior Java Developer");
        Assertions.assertThat(updatedVacancy.getBody()).isEqualTo("Updated description.");
    }

    @Test
    @DisplayName("Test 5: Delete Vacancy Test")
    public void deleteVacancyTest() {
        vacancyRepository.deleteById(vacancy.getId());
        Optional<Vacancy> deletedVacancy = vacancyRepository.findById(vacancy.getId());

        Assertions.assertThat(deletedVacancy).isEmpty();
    }
}
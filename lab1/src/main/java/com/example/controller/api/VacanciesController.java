package com.example.controller.api;

import com.example.dto.VacancyCreateDTO;
import com.example.dto.VacancyDTO;
import com.example.dto.VacancyUpdateDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.VacancyMapper;
import com.example.repository.VacancyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VacanciesController {

    @Autowired
    private VacancyRepository repository;

    @Autowired
    private VacancyMapper VacancyMapper;

    @GetMapping("/vacancies")
    @ResponseStatus(HttpStatus.OK)
    public List<VacancyDTO> index() {
        var Vacancies = repository.findAll();
        return Vacancies.stream()
                .map(VacancyMapper::map)
                .toList();
    }

    @PostMapping("/vacancies")
    @ResponseStatus(HttpStatus.CREATED)
    public VacancyDTO create(@Valid @RequestBody VacancyCreateDTO VacancyData) {
        var Vacancy = VacancyMapper.map(VacancyData);
        repository.save(Vacancy);
        return VacancyMapper.map(Vacancy);
    }

    @GetMapping("/vacancies/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VacancyDTO show(@PathVariable Long id) {
        var Vacancy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        return VacancyMapper.map(Vacancy);
    }

    @PutMapping("/vacancies/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VacancyDTO update(@RequestBody @Valid VacancyUpdateDTO vacancyData, @PathVariable Long id) {
        var vacancy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        VacancyMapper.update(vacancyData, vacancy);
        repository.save(vacancy);
        return VacancyMapper.map(vacancy);
    }

    @DeleteMapping("/vacancies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

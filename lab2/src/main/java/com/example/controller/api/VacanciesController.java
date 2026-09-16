package com.example.controller.api;

import com.example.dto.VacancyCreateDTO;
import com.example.dto.VacancyDTO;
import com.example.dto.VacancyUpdateDTO;
import com.example.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacanciesController {

    private final VacancyService vacancyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VacancyDTO> index() {
        return vacancyService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VacancyDTO show(@PathVariable Long id) {
        return vacancyService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VacancyDTO create(@Valid @RequestBody VacancyCreateDTO vacancyData) {
        return vacancyService.create(vacancyData);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VacancyDTO update(@PathVariable Long id, @Valid @RequestBody VacancyUpdateDTO vacancyData) {
        return vacancyService.update(vacancyData, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vacancyService.delete(id);
    }
}
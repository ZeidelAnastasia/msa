package com.example.service;

import com.example.dto.VacancyCreateDTO;
import com.example.dto.VacancyDTO;
import com.example.dto.VacancyUpdateDTO;

import java.util.List;

public interface VacancyService {

    List<VacancyDTO> getAll();

    VacancyDTO findById(Long id);

    VacancyDTO create(VacancyCreateDTO vacancyData);

    VacancyDTO update(VacancyUpdateDTO vacancyData, Long id);

    void delete(Long id);
}

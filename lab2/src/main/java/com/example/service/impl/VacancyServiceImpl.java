package com.example.service.impl;

import com.example.dto.VacancyCreateDTO;
import com.example.dto.VacancyDTO;
import com.example.dto.VacancyUpdateDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.VacancyMapper;
import com.example.repository.VacancyRepository;
import com.example.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository repository;
    private final VacancyMapper vacancyMapper;

    @Override
    public List<VacancyDTO> getAll() {
        var vacancies = repository.findAll();
        return vacancies.stream()
                .map(vacancyMapper::map)
                .toList();
    }

    @Override
    public VacancyDTO findById(Long id) {
        var vacancy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        return vacancyMapper.map(vacancy);
    }

    @Override
    @Transactional
    public VacancyDTO create(VacancyCreateDTO vacancyData) {
        var vacancy = vacancyMapper.map(vacancyData);
        repository.save(vacancy);
        return vacancyMapper.map(vacancy);
    }

    @Override
    @Transactional
    public VacancyDTO update(VacancyUpdateDTO vacancyData, Long id) {
        var vacancy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        vacancyMapper.update(vacancyData, vacancy);
        repository.save(vacancy);
        return vacancyMapper.map(vacancy);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

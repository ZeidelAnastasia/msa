package com.example.mapper;

import com.example.dto.VacancyCreateDTO;
import com.example.dto.VacancyDTO;
import com.example.dto.VacancyUpdateDTO;
import com.example.entity.Vacancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class VacancyMapper {

    @Mapping(target = "company", source = "companyId")
    public abstract Vacancy map(VacancyCreateDTO dto);

    @Mapping(source = "company.id", target = "companyId")
    public abstract VacancyDTO map(Vacancy model);

    @Mapping(target = "company", source = "companyId")
    public abstract void update(VacancyUpdateDTO dto, @MappingTarget Vacancy model);
}
package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VacancyCreateDTO {

    @NotNull
    private Long companyId;

    @NotNull
    private String link;

    @NotNull
    private String title;

    @NotNull
    private String body;
}


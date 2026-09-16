package com.example.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VacancyDTO {
    private Long id;
    private Long companyId;
    private String link;
    private String title;
    private String body;
    private LocalDate createdAt;
}


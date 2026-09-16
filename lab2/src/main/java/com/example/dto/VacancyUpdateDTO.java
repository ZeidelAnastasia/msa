package com.example.dto;

import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VacancyUpdateDTO {
    private JsonNullable<String> title = JsonNullable.undefined();
    private JsonNullable<String> body = JsonNullable.undefined();
    private JsonNullable<String> link = JsonNullable.undefined();
    private JsonNullable<Long> companyId = JsonNullable.undefined();
}


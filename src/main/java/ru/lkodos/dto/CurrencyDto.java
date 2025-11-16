package ru.lkodos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyDto {

    private Integer id;
    private String name;
    private String code;
    private String sign;
}
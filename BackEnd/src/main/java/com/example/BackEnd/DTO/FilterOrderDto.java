package com.example.BackEnd.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterOrderDto {
    private Long id;
    private Long customerId;
    private float fromPrice;
    private float toPrice;
    private String status;
}

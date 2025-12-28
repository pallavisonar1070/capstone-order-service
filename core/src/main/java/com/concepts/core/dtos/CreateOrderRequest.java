package com.concepts.core.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreateOrderRequest {
    private String userId;
    private BigDecimal amount;
}

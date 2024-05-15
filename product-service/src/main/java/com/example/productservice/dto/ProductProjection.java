package com.example.productservice.dto;

import java.math.BigDecimal;

public interface ProductProjection {
    String getId();
    String getName();
    String getDescription();
    BigDecimal getPrice();
}

package com.example.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InventoryResponse(
        String skuCode,
        boolean isInStock,
        Integer quantity
) {
    public InventoryResponse(String skuCode, boolean isInStock) {
        this(skuCode, isInStock, 0);
    }

    public InventoryResponse(String skuCode, Integer quantity) {
        this(skuCode, false, quantity);
    }
}

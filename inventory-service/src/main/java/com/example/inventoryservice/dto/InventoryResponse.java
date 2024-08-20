package com.example.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InventoryResponse(
        String skuCode,
        Boolean isInStock,
        Integer quantity
) {
    public InventoryResponse(String skuCode, boolean isInStock) {
        this(skuCode, isInStock, null);
    }

    public InventoryResponse(String skuCode, Integer quantity) {
        this(skuCode, null, quantity);
    }
}

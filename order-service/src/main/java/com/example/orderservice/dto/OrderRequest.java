package com.example.orderservice.dto;

import java.util.List;

public record OrderRequest(
        List<OrderLineItemDto> orderLineItemDtoList
) {
}

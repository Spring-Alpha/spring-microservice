package com.example.orderservice.dto;

import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        List<OrderLineItemDto> orderLineItemDtoList
) {
}

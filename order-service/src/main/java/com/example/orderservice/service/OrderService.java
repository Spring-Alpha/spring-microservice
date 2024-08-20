package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClient;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = orderLineItems.stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponses = webClient.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponses != null;
        boolean allProductInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if(allProductInStock){
            orderRepository.save(order);
            return "Order placed successfully!";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemDto orderLineItemDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemDto.price());
        orderLineItems.setQuantity(orderLineItemDto.quantity());
        orderLineItems.setSkuCode(orderLineItemDto.skuCode());

        return orderLineItems;
    }

    public List<OrderResponse> allOrder() {
        return orderRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderLineItemDto> orderLineItemDtos = order.getOrderLineItemsList()
                .stream().map(this::mapToItemResponse).toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                orderLineItemDtos);
    }

    private OrderLineItemDto mapToItemResponse(OrderLineItems orderLineItems) {
        return new OrderLineItemDto(
                orderLineItems.getId(),
                orderLineItems.getSkuCode(),
                orderLineItems.getPrice(),
                orderLineItems.getQuantity());
    }
}

package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream()
                .map(inventory ->
                        new InventoryResponse(
                                inventory.getSkuCode(),
                        inventory.getQuantity() > 0))
                .toList();
    }

    public List<InventoryResponse> allInventory() {
        return inventoryRepository.findAll().stream().map(inventory ->
                new InventoryResponse(inventory.getSkuCode(), inventory.getQuantity())).toList();
    }
}

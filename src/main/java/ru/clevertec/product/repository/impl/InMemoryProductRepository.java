package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.NullProductException;
import ru.clevertec.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private Map<UUID, Product> productMap;

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(productMap.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return productMap.values().stream().toList();
    }

    @Override
    public Product save(Product product) throws NullProductException {
        if (product == null) {
            throw new NullProductException();
        }
        if (product.getUuid() == null) {
            product.setUuid(UUID.randomUUID());
            productMap.put(product.getUuid(), product);
        } else {
            productMap.put(product.getUuid(), product);
        }
        return product;
    }

    @Override
    public void delete(UUID uuid) {
        productMap.remove(uuid);
    }

    public Map<UUID, Product> initDBData() {
        UUID PRODUCT_ONE_UUID = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf");
        UUID PRODUCT_TWO_UUID = UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2eda");

        productMap = new HashMap<>(Map.of(
                PRODUCT_ONE_UUID, new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко", "это однозначно коровье молоко",
                        BigDecimal.valueOf(3.99), LocalDateTime.of(2023, Month.OCTOBER, 29, 17, 30)),
                PRODUCT_TWO_UUID, new Product(PRODUCT_TWO_UUID, "Кефир", "это однозначно кефир",
                        BigDecimal.valueOf(2.99), LocalDateTime.of(2023, 10, 29, 17, 50))
        ));
        return productMap;
    }
}

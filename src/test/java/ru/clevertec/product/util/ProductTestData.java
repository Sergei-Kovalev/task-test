package ru.clevertec.product.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class ProductTestData {

    @Builder.Default
    private UUID uuid = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf");

    @Builder.Default
    private String name = "Молоко";

    @Builder.Default
    private String description = "это однозначно коровье молоко";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(3.99);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.of(2023, Month.OCTOBER, 29, 17, 30);

    public Product buildProduct() {
        return new Product(uuid, name, description, price, created);
    }
    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }
    public InfoProductDto buildInfoProduct() {
        return new InfoProductDto(uuid, name, description, price);
    }
}
package ru.clevertec.product.validators;

import ru.clevertec.product.data.ProductDto;

import java.math.BigDecimal;

public class ProductDtoValidator {
    public static ProductDto validate(ProductDto productDto) {
        checkName(productDto.name());
        checkDescription(productDto.description());
        checkPrice(productDto.price());
        return productDto;
    }

    private static void checkName(String name) throws IllegalArgumentException {
        if (!name.matches("[а-яёА-ЯЁ\\s]{5,10}")) {
            throw new IllegalArgumentException(
                    "Название продукта (не может быть null или пустым, содержит 5-10 символов(русский или пробелы))");
        }
    }

    private static void checkDescription(String description) throws IllegalArgumentException {
        if (!description.matches("[а-яёА-ЯЁ\\s]{10,30}")) {
            throw new IllegalArgumentException(
                    "Описание продукта(может быть null или 10-30 символов(русский и пробелы))");
        }
    }

    private static void checkPrice(BigDecimal price) throws IllegalArgumentException {
        if (price == null) {
            throw new IllegalArgumentException("Цена не может быть null");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
    }
}

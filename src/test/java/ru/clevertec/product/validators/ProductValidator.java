package ru.clevertec.product.validators;

import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductValidator {
    public static Product validate(Product product) {
        checkName(product.getName());
        checkDescription(product.getDescription());
        checkPrice(product.getPrice());
        checkCreated(product.getCreated());
        return product;
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

    private static void checkCreated(LocalDateTime time) throws IllegalArgumentException {
        if (time == null) {
            throw new IllegalArgumentException("Время создания, не может быть null");
        }
    }
}

package ru.clevertec.product.validators;

import ru.clevertec.product.data.InfoProductDto;

import java.math.BigDecimal;
import java.util.UUID;

public class InfoProductDtoValidator {
    public static InfoProductDto validate(InfoProductDto infoProductDto) {
        checkUuid(infoProductDto.uuid());
        checkName(infoProductDto.name());
        checkDescription(infoProductDto.description());
        checkPrice(infoProductDto.price());
        return infoProductDto;
    }

    private static void checkUuid(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("Идентификатор не может быть null");
        }
    }

    private static void checkName(String name) throws IllegalArgumentException {
        if (!name.matches("[а-яёА-ЯЁ\\s]{5,10}")) {
            throw new IllegalArgumentException(
                    "Название продукта (не может быть null или пустым, содержит 5-10 символов(русский или пробелы))");
        }
    }

    private static void checkDescription(String description) throws IllegalArgumentException {
        if (description == null) {
            throw new IllegalArgumentException(
                    "Описание продукта не может быть null");
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

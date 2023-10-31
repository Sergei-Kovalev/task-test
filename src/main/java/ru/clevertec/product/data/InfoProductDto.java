package ru.clevertec.product.data;

import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.UUID;

@FieldNameConstants
public record InfoProductDto(

        /**
         * Идентификатор не может быть null
         */
        UUID uuid,

        /**
         * Имя продукта смотрите {@link ru.clevertec.product.entity.Product}
         */
        String name,

        /**
         * Описание продукта не может быть null, может быть пустой строкой
         */
        String description,

        /**
         * Стоимость не может быть null или негативным
         */
        BigDecimal price) {
}

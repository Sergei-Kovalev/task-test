package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.util.ProductTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
    }

    @Nested
    class FindByIdTests {

        @Test
        void findByIdShouldReturnProductClassTypeWithRealUUIDFromDB() {
            // given
            UUID uuid = ProductTestData.builder().build().getUuid();

            // when
            var actual = productRepository.findById(uuid).orElseThrow();

            // then
            assertThat(actual).isInstanceOf(Product.class);
        }
        @Test
        void findByIdShouldReturnOptionalClassTypeWhenUUIDIsNotExistInDB() {
            // given
            UUID uuid = UUID.randomUUID();

            // when
            var actual = productRepository.findById(uuid);

            // then
            assertThat(actual).isInstanceOf(Optional.class);
        }
        @Test
        void findByIdShouldReturnEmptyOptionalWhenUUIDIsNotExistInDB() {
            // given
            UUID uuid = UUID.randomUUID(); //с таким не добавлять.. его типа нет в бд
            Optional<Product> expected = Optional.empty();

            // when
            var actual = productRepository.findById(uuid);

            // then
            assertThat(actual).isEqualTo(expected);
        }
        @Test
        void findByIdShouldReturnProductWithSameFields() {
            // given
            Product expected = ProductTestData.builder().build().buildProduct();

            // when
            Product actual = productRepository.findById(expected.getUuid()).orElseThrow();

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
        }
        @Test
        void findByIdShouldReturnProductWithFieldRequirements() {
            // given
            UUID uuid = ProductTestData.builder().build().getUuid();

            // when
            Product actual = productRepository.findById(uuid).orElseThrow();

            // then
            assertThat(actual.getName())
                    .isNotNull()
                    .isNotEmpty()
                    .matches("[а-яёА-ЯЁ\\s]{5,10}");

            assertThat(actual.getDescription())
                    .matches(str -> str == null || str.matches("[а-яёА-ЯЁ\\s]{10,30}"));

            assertThat(actual.getPrice())
                    .isNotNull()
                    .isPositive();

            assertThat(actual.getCreated())
                    .isNotNull();
        }
    }

    @Nested
    class FindAllTests {

        @Test
        void findAll() {
            // given
            Product product1 = ProductTestData.builder().build().buildProduct();
            Product product2 = ProductTestData.builder()
                    .withUuid(UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2eda"))
                    .withName("Кефир")
                    .withDescription("это однозначно кефир")
                    .withPrice( BigDecimal.valueOf(2.99))
                    .withCreated(LocalDateTime.of(2023, 10, 29, 17, 50))
                    .build().buildProduct();

            List<Product> expected = List.of(product1, product2);

            // when
            List<Product> actual = productRepository.findAll();

            // then
            assertThat(actual)
                    .isNotEmpty()
                    .hasSameSizeAs(expected)
                    .hasOnlyElementsOfType(Product.class);
        }
    }

    @Nested
    class SaveTests {
        @Test
        void saveWhenProductExist() {
            // given
            Product expected = ProductTestData.builder().build().buildProduct();

            // when
            Product actual = productRepository.save(expected);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
        }
        @Test
        void saveWhenProductIsNull() {
            // given
            Product expected = null;

            // when, then
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> productRepository.save(expected));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void delete() {
            // given
            UUID uuid = ProductTestData.builder().build().getUuid();

            // when
            Optional<Product> beforeDelete = productRepository.findById(uuid);
            productRepository.delete(uuid);
            Optional<Product> actual = productRepository.findById(uuid);

            // then
            assertThat(beforeDelete)
                    .isPresent();

            assertThat(actual)
                    .isEmpty();
        }
    }
}
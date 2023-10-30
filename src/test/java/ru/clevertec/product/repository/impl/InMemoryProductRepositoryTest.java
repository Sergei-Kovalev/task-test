package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
            UUID uuid = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"); //реализовать с таким UUID

            // when
            var actual = productRepository.findById(uuid).orElseThrow();

            // then
            assertThat(actual).isInstanceOf(Product.class);
        }
        @Test
        void findByIdShouldReturnOptionalClassTypeWhenUUIDIsNotExistInDB() {
            // given
            UUID uuid = UUID.fromString("b23c446a-cfb1-4bf2-9713-a283f0fe2f48"); //с таким не добавлять.. его типа нет в бд

            // when
            var actual = productRepository.findById(uuid);

            // then
            assertThat(actual).isInstanceOf(Optional.class);
        }
        @Test
        void findByIdShouldReturnEmptyOptionalWhenUUIDIsNotExistInDB() {
            // given
            UUID uuid = UUID.fromString("b23c446a-cfb1-4bf2-9713-a283f0fe2f48"); //с таким не добавлять.. его типа нет в бд
            Optional<Product> expected = Optional.empty();

            // when
            var actual = productRepository.findById(uuid);

            // then
            assertThat(actual).isEqualTo(expected);
        }
        @Test
        void findByIdShouldReturnProductWithSameFields() {
            // given
            UUID uuid = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"); //реализовать с таким UUID
            Product expected = new Product(
                    uuid, "Молоко", "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                    LocalDateTime.of(2023, 10, 29, 17, 30));

            // when
            Product actual = productRepository.findById(uuid).orElseThrow();

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
        } @Test
        void findByIdShouldReturnProductWithFieldRequirements() {
            // given
            UUID uuid = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"); //реализовать с таким UUID

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
            UUID uuid1 = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"); //реализовать с таким UUID
            UUID uuid2 = UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2eda"); //и лист с 2мя продуктами
            Product product1 = new Product(
                    uuid1, "Молоко", "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                    LocalDateTime.of(2023, 10, 29, 17, 30));
            Product product2 = new Product(
                    uuid2, "Кефир", "это однозначно кефир", BigDecimal.valueOf(2.99),
                    LocalDateTime.of(2023, 10, 29, 17, 50));
            List<Product> expected = List.of(product1, product2);

            // when
            List<Product> actual = productRepository.findAll();

            // then
            assertThat(actual)
                    .isNotEmpty()
                    .hasSameSizeAs(expected)
                    .hasOnlyElementsOfType(Product.class)
            ;
        }
    }

    @Nested
    class SaveTests {
        @Test
        void saveWhenProductExist() {
            // given
            UUID uuid = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"); //реализовать с таким UUID
            Product expected = new Product(
                    uuid, "Молоко", "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                    LocalDateTime.of(2023, 10, 29, 17, 30));
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

            // when
            Product actual = productRepository.save(expected);

            // then
            assertThat(actual)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void delete() {
            // given
            UUID uuid = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"); //реализовать с таким UUID

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
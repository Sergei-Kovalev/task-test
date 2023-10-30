package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperImplTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapperImpl();
    }

    @Nested
    class toProductMethodTests {

        @Test
        void toProductTest() {
            // given
            UUID uuid = UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"); //реализовать с таким UUID
            Product expected = new Product(
                    uuid, "Молоко", "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                    LocalDateTime.of(2023, 10, 29, 17, 30));

            ProductDto productDto = new ProductDto(
                    "Молоко", "это однозначно коровье молоко", BigDecimal.valueOf(3.99));

            // when
            Product actual = productMapper.toProduct(productDto);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice());
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.product.mapper.impl.ProductMapperImplTest#getArgumentsForToProductTest")
        void toProductParameterizedTest(Product product, ProductDto productDto) {
            // given - всё в аргументах

            // when
            Product actual = productMapper.toProduct(productDto);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.name, product.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, product.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, product.getPrice());
        }
    }

    @Nested
    class toInfoProductDtoTests {
        @Test
        void toInfoProductDtoWithoutParameters() {
            // given
            Product product = new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                    "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                    LocalDateTime.of(2023, 10, 29, 17, 30));
            InfoProductDto expected = new InfoProductDto(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                    "это однозначно коровье молоко", BigDecimal.valueOf(3.99));

            // when
            InfoProductDto actual = productMapper.toInfoProductDto(product);

            // then
            assertThat(actual)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.uuid, expected.uuid())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.price, expected.price());
        }
        @ParameterizedTest
        @MethodSource("ru.clevertec.product.mapper.impl.ProductMapperImplTest#getArgumentsForToInfoProductTest")
        void toInfoProductDtoParametrizedTest(Product product, InfoProductDto expected) {
            // given из аргументов, передаваемых в параметры метода

            // when
            InfoProductDto actual = productMapper.toInfoProductDto(product);

            // then
            assertThat(actual)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.uuid, expected.uuid())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.price, expected.price());
        }

    }

    @Nested
    class mergeTests {
        @Test
        void mergeWithoutParameters() {
            // given
            Product product = new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                    "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                    LocalDateTime.of(2023, 10, 29, 17, 30));
            ProductDto productDto = new ProductDto("Молоко",
                    "это однозначно коровье молоко", BigDecimal.valueOf(4.00)); // изменил цену

            Product expected = new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                    "это однозначно коровье молоко", BigDecimal.valueOf(4.00),
                    LocalDateTime.of(2023, 10, 29, 17, 30));

            // when
            Product actual = productMapper.merge(product, productDto);

            // then
            assertThat(actual)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getPrice());
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.product.mapper.impl.ProductMapperImplTest#getArgumentsForMergeMethod")
        void mergeParametrizedTest(Product product, ProductDto productDto, Product expected) {
            // given из аргументов метода

            // when
            Product actual = productMapper.merge(product, productDto);

            // then
            assertThat(actual)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getPrice());
        }
    }

    public static Stream<Arguments> getArgumentsForMergeMethod() {
        return Stream.of(
                Arguments.of(new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                        "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                        LocalDateTime.of(2023, 10, 29, 17, 30)),
                        new ProductDto("Молоко",
                                "это однозначно коровье молоко", BigDecimal.valueOf(4.00)), // изменение цены
                        new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                                "это однозначно коровье молоко", BigDecimal.valueOf(4.00),
                                LocalDateTime.of(2023, 10, 29, 17, 30))
                        ),
                Arguments.of(new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                        "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                        LocalDateTime.of(2023, 10, 29, 17, 30)),
                        new ProductDto("Напиток",
                                "это однозначно коровье молоко", BigDecimal.valueOf(3.99)), // изменение наименования
                        new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Напиток",
                                "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                                LocalDateTime.of(2023, 10, 29, 17, 30))
                        ),
                Arguments.of(new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                        "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                        LocalDateTime.of(2023, 10, 29, 17, 30)),
                        new ProductDto("Напиток",
                                "это точно не коровье молоко", BigDecimal.valueOf(1.01)), // изменение всего
                        new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Напиток",
                                "это точно не коровье молоко", BigDecimal.valueOf(1.01),
                                LocalDateTime.of(2023, 10, 29, 17, 30))
                        )
        );
    }

    public static Stream<Arguments> getArgumentsForToInfoProductTest() {
        return Stream.of(
                Arguments.of(
                        new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                                "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                                LocalDateTime.of(2023, 10, 29, 17, 30)),
                        new InfoProductDto(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                        "это однозначно коровье молоко", BigDecimal.valueOf(3.99))
                ),
                Arguments.of(
                        new Product(UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2ed2"), "Кефир",
                                "это однозначно из коровьего молока", BigDecimal.valueOf(2.99),
                                LocalDateTime.of(2023, 10, 30, 17, 50)),
                        new InfoProductDto(UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2ed2"), "Кефир",
                                "это однозначно из коровьего молока", BigDecimal.valueOf(2.99))
                )
        );
    }

    public static Stream<Arguments> getArgumentsForToProductTest() {
        return Stream.of(
                Arguments.of(
                        new Product(UUID.fromString("18a9cc59-c7c7-47e2-ac77-d3127d3b2edf"), "Молоко",
                                "это однозначно коровье молоко", BigDecimal.valueOf(3.99),
                                LocalDateTime.of(2023, 10, 29, 17, 30)),
                        new ProductDto("Молоко", "это однозначно коровье молоко", BigDecimal.valueOf(3.99))
                ),
                Arguments.of(
                        new Product(UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2ed2"), "Кефир",
                                "это однозначно из коровьего молока", BigDecimal.valueOf(2.99),
                                LocalDateTime.of(2023, 10, 30, 17, 50)),
                        new ProductDto("Кефир", "это однозначно из коровьего молока", BigDecimal.valueOf(2.99))
                )
        );
    }
}
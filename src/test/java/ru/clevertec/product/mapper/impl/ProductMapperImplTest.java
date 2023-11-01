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
import ru.clevertec.product.util.ProductTestData;

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
            Product expected = ProductTestData.builder().build().buildProduct();

            ProductDto productDto = ProductTestData.builder().build().buildProductDto();

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
            Product product = ProductTestData.builder().build().buildProduct();
            InfoProductDto expected = ProductTestData.builder().build().buildInfoProduct();

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
            Product product = ProductTestData.builder().build().buildProduct();
            ProductDto productDto = ProductTestData.builder()
                    .withPrice(BigDecimal.valueOf(4.00)) // изменил цену
                    .build().buildProductDto();

            Product expected = ProductTestData.builder()
                    .withPrice(BigDecimal.valueOf(4.00)) // с измененной ценой
                    .build().buildProduct();

            // when
            Product actual = productMapper.merge(product, productDto);

            // then
            assertThat(actual)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
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
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
        }
    }

    public static Stream<Arguments> getArgumentsForMergeMethod() {
        return Stream.of(
                Arguments.of(
                        ProductTestData.builder().build().buildProduct(),
                        ProductTestData.builder()
                                .withPrice(BigDecimal.valueOf(4.00)) // изменение цены
                                .build().buildProductDto(),
                        ProductTestData.builder()
                                .withPrice(BigDecimal.valueOf(4.00))
                                .build().buildProduct()
                        ),
                Arguments.of(
                        ProductTestData.builder().build().buildProduct(),
                        ProductTestData.builder()
                                .withName("Напиток") // изменение наименования
                                .build().buildProductDto(),
                        ProductTestData.builder()
                                .withName("Напиток")
                                .build().buildProduct()
                        ),
                Arguments.of(
                        ProductTestData.builder().build().buildProduct(),
                        ProductTestData.builder()
                                .withName("Напиток")                                // изменение всех полей
                                .withDescription("это точно не коровье молоко")
                                .withPrice(BigDecimal.valueOf(1.01))
                                .build().buildProductDto(),
                        ProductTestData.builder()
                                .withName("Напиток")
                                .withDescription("это точно не коровье молоко")
                                .withPrice(BigDecimal.valueOf(1.01))
                                .build().buildProduct()
                        )
        );
    }

    public static Stream<Arguments> getArgumentsForToInfoProductTest() {
        return Stream.of(
                Arguments.of(
                        ProductTestData.builder().build().buildProduct(),
                        ProductTestData.builder().build().buildInfoProduct()
                ),
                Arguments.of(
                        ProductTestData.builder()
                                .withUuid(UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2ed2"))
                                .withName("Кефир")
                                .withDescription("это однозначно из коровьего молока")
                                .withPrice(BigDecimal.valueOf(2.99))
                                .withCreated(LocalDateTime.of(2023, 10, 30, 17, 50))
                                .build().buildProduct(),
                        ProductTestData.builder()
                                .withUuid(UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2ed2"))
                                .withName("Кефир")
                                .withDescription("это однозначно из коровьего молока")
                                .withPrice(BigDecimal.valueOf(2.99))
                                .build().buildInfoProduct()
                )
        );
    }

    public static Stream<Arguments> getArgumentsForToProductTest() {
        return Stream.of(
                Arguments.of(
                        ProductTestData.builder().build().buildProduct(),
                        ProductTestData.builder().build().buildProductDto()
                ),
                Arguments.of(
                        ProductTestData.builder()
                                .withUuid(UUID.fromString("28a9cc59-c7c7-47e2-ac77-d3127d3b2ed2"))
                                .withName("Кефир")
                                .withDescription("это однозначно из коровьего молока")
                                .withPrice(BigDecimal.valueOf(2.99))
                                .withCreated(LocalDateTime.of(2023, 10, 30, 17, 50))
                                .build().buildProduct(),
                        ProductTestData.builder()
                                .withName("Кефир")
                                .withDescription("это однозначно из коровьего молока")
                                .withPrice(BigDecimal.valueOf(2.99))
                                .build().buildProductDto()
                )
        );
    }
}
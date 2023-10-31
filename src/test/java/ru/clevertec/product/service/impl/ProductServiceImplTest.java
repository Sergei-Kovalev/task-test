package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.util.ProductTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductMapper mapper;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Nested
    class GetMethodTests {
        @Test
        void getShouldReturnInfoProductDtoWhenUuidExistInDB() {
            // given
            UUID uuid = ProductTestData.builder().build().getUuid();
            Product productFromDB = ProductTestData.builder()
                    .withUuid(uuid)
                    .build().buildProduct();
            InfoProductDto expected = ProductTestData.builder()
                    .withUuid(uuid)
                    .build().buildInfoProduct();
            doReturn(Optional.of(productFromDB))
                    .when(productRepository).findById(uuid);
            when(mapper.toInfoProductDto(productFromDB))
                    .thenReturn(expected);
            // when
            InfoProductDto actual = productService.get(uuid);

            // then
            assertThat(actual)
                    .isNotNull()
                    .isInstanceOf(InfoProductDto.class)
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.uuid, expected.uuid())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(InfoProductDto.Fields.price, expected.price());
        }
        @Test
        void getShouldReturnInfoProductDtoWhenUuidIsNotExistInDB() {
            // given
            UUID uuid = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(productRepository).findById(uuid);

            // when, then
            assertThatThrownBy(() -> productService.get(uuid))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage(String.format("Product with uuid: %s not found", uuid));
        }
    }

    @Nested
    class GetAllMethodTests {
        @Test
        void getAllShouldReturnListFromTwoElementsThatMustBeTheSameAsExpected() {
            // given
            List<Product> productsFromDB = new ArrayList<>();
            Product product1 = ProductTestData.builder().build().buildProduct();
            Product product2 = ProductTestData.builder()
                    .withUuid(UUID.fromString("cd56ff80-1e5a-445d-b0ce-188c471f5804"))
                    .build().buildProduct();
            productsFromDB.add(product1);
            productsFromDB.add(product2);

            List<InfoProductDto> expected = new ArrayList<>();
            InfoProductDto expectedInfoProductDto1 = ProductTestData.builder().build().buildInfoProduct();
            InfoProductDto expectedInfoProductDto2 = ProductTestData.builder()
                    .withUuid(UUID.fromString("cd56ff80-1e5a-445d-b0ce-188c471f5804"))
                    .build().buildInfoProduct();
            expected.add(expectedInfoProductDto1);
            expected.add(expectedInfoProductDto2);

            doReturn(productsFromDB)
                    .when(productRepository).findAll();
            when(mapper.toInfoProductDto(product1))
                    .thenReturn(expectedInfoProductDto1);
            when(mapper.toInfoProductDto(product2))
                    .thenReturn(expectedInfoProductDto2);

            // when
            List<InfoProductDto> actual = productService.getAll();

            // then
            assertThat(actual)
                    .isNotNull()
                    .hasSameClassAs(expected)
                    .hasSameSizeAs(expected)
                    .containsExactlyInAnyOrder(expectedInfoProductDto1, expectedInfoProductDto2);
        }
    }

    @Nested
    class CreateMethodTests {
        @Test
        void createShouldInvokeRepositoryWithoutUuid() {
            // given
            Product productToSave = ProductTestData.builder()
                    .withUuid(null)
                    .build().buildProduct();
            Product expected = ProductTestData.builder().build().buildProduct();
            ProductDto productDto = ProductTestData.builder()
                    .withUuid(null)
                    .build().buildProductDto();

            doReturn(expected)
                    .when(productRepository).save(productToSave);
            when(mapper.toProduct(productDto))
                    .thenReturn(productToSave);

            // when
            productService.create(productDto);

            // then
            verify(productRepository).save(productCaptor.capture());
            assertThat(productCaptor.getValue())
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, null);
        }
        @Test
        void createShouldReturnUuid() {
            // given
            Product productToSave = ProductTestData.builder()
                    .withUuid(null)
                    .build().buildProduct();
            ProductDto productDto = ProductTestData.builder()
                    .withUuid(null)
                    .build().buildProductDto();
            UUID expected = ProductTestData.builder().build().buildProduct().getUuid();
            Product productAfterSaving = ProductTestData.builder()
                    .withUuid(expected)
                    .build().buildProduct();

            doReturn(productAfterSaving)
                    .when(productRepository).save(productToSave);
            when(mapper.toProduct(productDto))
                    .thenReturn(productToSave);

            // when
            UUID actual = productService.create(productDto);

            // then
            verify(productRepository).save(productToSave);
            assertThat(actual)
                    .isNotNull()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class UpdateMethodTests {
        @Test
        void updateShouldInvokeSaveProductToRepositoryWithSameFields() {
            // given
            UUID uuid = ProductTestData.builder().build().getUuid();

            Product productToUpdate = ProductTestData.builder()
                    .build().buildProduct();
            Product productAfterMerge = ProductTestData.builder()
                    .withName("Кефир")
                    .build().buildProduct();
            Product expected = ProductTestData.builder()
                    .withName("Кефир")
                    .build().buildProduct();
            ProductDto productDto = ProductTestData.builder()
                    .withName("Кефир")
                    .build().buildProductDto();

            doReturn(Optional.of(productToUpdate))
                    .when(productRepository).findById(uuid);
            doReturn(expected)
                    .when(productRepository).save(productAfterMerge);
            when(mapper.merge(productToUpdate, productDto))
                    .thenReturn(productAfterMerge);

            // when
            productService.update(uuid, productDto);

            // then
            verify(productRepository).save(productCaptor.capture());
            assertThat(productCaptor.getValue())
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
        }
    }

    @Nested
    class DeleteMethodTests {
        @Test
        void delete() {
            // given
            UUID uuid = ProductTestData.builder().build().getUuid();

            // when
            productService.delete(uuid);

            // then
            verify(productRepository)
                    .delete(uuid);
        }
    }
}
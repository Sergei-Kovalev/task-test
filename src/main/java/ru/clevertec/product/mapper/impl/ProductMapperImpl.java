package ru.clevertec.product.mapper.impl;

import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;

public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toProduct(ProductDto productDto) {
        return ProductMapper.INSTANCE.toProduct(productDto);
    }

    @Override
    public InfoProductDto toInfoProductDto(Product product) {
        return ProductMapper.INSTANCE.toInfoProductDto(product);
    }

    @Override
    public Product merge(Product product, ProductDto productDto) {
        return ProductMapper.INSTANCE.merge(product, productDto);
    }
}

package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    @Override
    public InfoProductDto get(UUID uuid) {
        Optional<Product> optFromDB = productRepository.findById(uuid);
        Product productFromDB = optFromDB.orElseThrow(() ->new ProductNotFoundException(uuid));
        return mapper.toInfoProductDto(productFromDB);
    }

    @Override
    public List<InfoProductDto> getAll() {
        return productRepository.findAll().stream()
                .map(mapper::toInfoProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product productToSaveInDB = mapper.toProduct(productDto);
        Product saved = productRepository.save(productToSaveInDB);
        return saved.getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Product productForUpdate = productRepository.findById(uuid).orElseThrow();
        Product updatedProductToSave = mapper.merge(productForUpdate, productDto);
        productRepository.save(updatedProductToSave);
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}

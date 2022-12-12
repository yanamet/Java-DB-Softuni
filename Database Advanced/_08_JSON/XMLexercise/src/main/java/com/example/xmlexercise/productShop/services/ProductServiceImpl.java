package com.example.xmlexercise.productShop.services;

import com.example.xmlexercise.productShop.entities.categories.CategoriesExportDTO;
import com.example.xmlexercise.productShop.entities.categories.CategoryStats;
import com.example.xmlexercise.productShop.entities.categories.CategoryStatsDTO;
import com.example.xmlexercise.productShop.entities.products.Product;
import com.example.xmlexercise.productShop.entities.products.ExportProductsInRange;
import com.example.xmlexercise.productShop.entities.products.ProductWithAttributesDTO;
import com.example.xmlexercise.productShop.entities.users.User;
import com.example.xmlexercise.productShop.repositories.ProductRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper mapper;
    private final TypeMap<Product, ProductWithAttributesDTO> productToDtoMapping;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;

        this.mapper = new ModelMapper();

        TypeMap<Product, ProductWithAttributesDTO> typeMap =
                this.mapper.createTypeMap(Product.class, ProductWithAttributesDTO.class);

        Converter<User, String> userToFullNameConverter =
                context -> context == null ? null : context.getSource().getFullName();

        this.productToDtoMapping = typeMap.addMappings(map ->
                map.using(userToFullNameConverter)
                        .map(Product::getSeller, ProductWithAttributesDTO::setSeller));


        this.mapper.addConverter(userToFullNameConverter);
    }

    @Override
    public ExportProductsInRange getInRange(float from, float to) {
        BigDecimal fromDec = BigDecimal.valueOf(from);
        BigDecimal toDec = BigDecimal.valueOf(to);

        List<Product> products = this.productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(fromDec, toDec);

        List<ProductWithAttributesDTO> productsDto =
                products
                        .stream()
                        .map(this.productToDtoMapping::map)
                        .collect(Collectors.toList());

        return new ExportProductsInRange(productsDto);
    }

    @Override
    public CategoriesExportDTO getCategoryStatistics() {
        List<CategoryStats> categoryStats = this.productRepository.getCategoryStats();

        List<CategoryStatsDTO> dtos = categoryStats
                .stream()
                .map(c -> this.mapper.map(c, CategoryStatsDTO.class))
                .collect(Collectors.toList());

        return new CategoriesExportDTO(dtos);
    }


}

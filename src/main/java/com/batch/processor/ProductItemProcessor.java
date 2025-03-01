package com.batch.processor;

import com.batch.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);

    @Override
    public Product process(final Product product) {
        // Apply a 10% discount to all products
        final BigDecimal discountedPrice = product.getPrice().multiply(new BigDecimal("0.9")).setScale(2, BigDecimal.ROUND_HALF_UP);
        
        final Product transformedProduct = new Product(
                product.getId(),
                product.getName().toUpperCase(),
                product.getDescription(),
                discountedPrice,
                product.getStock()
        );

        log.info("Converting (" + product + ") into (" + transformedProduct + ")");

        return transformedProduct;
    }
}
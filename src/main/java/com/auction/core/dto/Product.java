package com.auction.core.dto;

import com.auction.core.enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
public class Product {
    private String id;
    private String name;
    private String description;
    private List<String> images;
    private Integer basePrice;
    private ProductStatus status;
}

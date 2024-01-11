package com.hcmute.drink.model.elasticsearch;

import com.hcmute.drink.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Setting(settingPath = "/config/elasticsearch/product/setting.json")
@Mapping(mappingPath = "/config/elasticsearch/product/mapping.json")
@Document(indexName = "product")
public class ProductIndex {
    private String id;
    private String code;
    private String name;
    private String thumbnail;
    private String description;
    private double price;
    private ProductStatus status;
}

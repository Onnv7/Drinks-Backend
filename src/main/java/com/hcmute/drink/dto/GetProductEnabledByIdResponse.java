package com.hcmute.drink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.collection.embedded.SizeEmbedded;
import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import lombok.Data;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
public class GetProductEnabledByIdResponse {
    @Id
    private String id;
    private String name;
    @JsonProperty("imageUrl")
    private String image;
    private List<SizeEmbedded> sizeList;
    private String description;
    @UniqueElements
    private List<ToppingEmbedded> toppingList;
}

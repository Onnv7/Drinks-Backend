package com.hcmute.drink.collection;

import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.enums.BannerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "banner")
public class BannerCollection {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private ImageEmbedded image;
    private BannerStatus status;
    private boolean isDeleted;
}

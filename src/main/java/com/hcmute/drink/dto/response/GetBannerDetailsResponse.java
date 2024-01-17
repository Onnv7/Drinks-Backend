package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.enums.BannerStatus;
import lombok.Data;

@Data
public class GetBannerDetailsResponse {
    private String id;
    private String name;
    @JsonProperty("imageUrl")
    private String image;
    private BannerStatus status;
}

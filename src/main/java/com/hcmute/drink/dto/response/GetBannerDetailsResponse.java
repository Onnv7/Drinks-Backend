package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.BannerStatus;
import lombok.Data;

@Data
public class GetBannerDetailsResponse {
    private String id;
    private String name;
    private String imageUrl;
    private BannerStatus status;
}

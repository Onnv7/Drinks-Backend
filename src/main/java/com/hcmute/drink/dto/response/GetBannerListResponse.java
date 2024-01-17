package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.BannerStatus;
import lombok.Data;

@Data
public class GetBannerListResponse {
    private String id;
    private String name;
    private BannerStatus status;
}

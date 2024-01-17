package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateBannerRequest;
import com.hcmute.drink.dto.request.UpdateBannerRequest;
import com.hcmute.drink.dto.response.GetBannerDetailsResponse;
import com.hcmute.drink.dto.response.GetBannerListResponse;
import com.hcmute.drink.dto.response.GetVisibleBannerListResponse;

import java.util.List;

public interface IBannerService {
    void createBanner(CreateBannerRequest body);
    void updateBannerById(UpdateBannerRequest body, String bannerId);
    void deleteBannerById(String bannerId);
    List<GetBannerListResponse> getBannerList();
    List<GetVisibleBannerListResponse> getVisibleBannerList();
    GetBannerDetailsResponse getBannerDetailsById(String id);

}

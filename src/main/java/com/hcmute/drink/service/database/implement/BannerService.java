package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.BannerCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateBannerRequest;
import com.hcmute.drink.dto.request.UpdateBannerRequest;
import com.hcmute.drink.dto.response.GetBannerDetailsResponse;
import com.hcmute.drink.dto.response.GetBannerListResponse;
import com.hcmute.drink.dto.response.GetVisibleBannerListResponse;
import com.hcmute.drink.enums.BannerStatus;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.BannerRepository;
import com.hcmute.drink.service.common.CloudinaryService;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.service.database.IBannerService;
import com.hcmute.drink.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService implements IBannerService {
    private final BannerRepository bannerRepository;
    private final ModelMapperService modelMapperService;
    private final CloudinaryService cloudinaryService;

    public BannerCollection getById(String bannerId) {
        return bannerRepository.findById(bannerId).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + bannerId));
    }
    @Override
    public void createBanner(CreateBannerRequest body) {
        BannerCollection banner = modelMapperService.mapClass(body, BannerCollection.class);

        try {
            HashMap<String, String> bannerImage = cloudinaryService.uploadFileToFolder(
                    CloudinaryConstant.BANNER_PATH,
                    StringUtils.generateFileName(body.getName(), "banner"),
                    body.getImage().getBytes()
            );
            ImageEmbedded image = new ImageEmbedded(bannerImage.get(CloudinaryConstant.PUBLIC_ID), bannerImage.get(CloudinaryConstant.URL_PROPERTY));
            banner.setImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        banner.setDeleted(false);
        banner.setStatus(BannerStatus.HIDDEN);
        bannerRepository.save(banner);
    }
    @Override
    public void updateBannerById(UpdateBannerRequest body, String bannerId) {
        BannerCollection bannerCollection = getById(bannerId);
        modelMapperService.map(body, bannerCollection);
        if(bannerCollection.getImage() != null) {
            try {
                // TODO: kiểm tra lại tính transaction khi xóa thành công -> upload new không thành công
                cloudinaryService.deleteImage(bannerCollection.getImage().getId());

                HashMap<String, String> bannerImage = cloudinaryService.uploadFileToFolder(
                        CloudinaryConstant.BANNER_PATH,
                        StringUtils.generateFileName(body.getName(), "banner"),
                        body.getImage().getBytes()
                );
                ImageEmbedded image = new ImageEmbedded(bannerImage.get(CloudinaryConstant.PUBLIC_ID), bannerImage.get(CloudinaryConstant.URL_PROPERTY));
                bannerCollection.setImage(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        bannerRepository.save(bannerCollection);
    }

    @Override
    public void deleteBannerById(String bannerId) {
        BannerCollection bannerCollection = getById(bannerId);
        bannerCollection.setDeleted(true);
        bannerRepository.save(bannerCollection);
    }

    @Override
    public List<GetBannerListResponse> getBannerList() {
        List<BannerCollection> bannerCollectionList = bannerRepository.getAll();
        return modelMapperService.mapList(bannerCollectionList, GetBannerListResponse.class);
    }

    @Override
    public List<GetVisibleBannerListResponse> getVisibleBannerList() {
        return bannerRepository.getVisibleBannerList();
    }

    @Override
    public GetBannerDetailsResponse getBannerDetailsById(String id) {
        return bannerRepository.getBannerDetailsById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

}

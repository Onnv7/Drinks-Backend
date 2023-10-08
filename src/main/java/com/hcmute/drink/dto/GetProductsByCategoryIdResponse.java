package com.hcmute.drink.dto;

import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.SizeModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;

import java.util.List;

@Data
public class GetProductsByCategoryIdResponse {
    private String id;
    private String name;
    private List<ImageModel> imageList;
}

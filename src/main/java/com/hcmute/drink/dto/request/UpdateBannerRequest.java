package com.hcmute.drink.dto.request;

import com.hcmute.drink.enums.BannerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateBannerRequest {
    @Schema(example = BANNER_NAME_EX)
    @NotBlank
    private String name;

    @Schema()
    private MultipartFile image;

    @Schema(example = BANNER_STATUS_EX)
    @NotNull
    private BannerStatus status;
}

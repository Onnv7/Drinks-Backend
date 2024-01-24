package com.hcmute.drink.service.common;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.model.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.hcmute.drink.constant.CloudinaryConstant.*;

@Component
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    // TODO: co the xoas
    public String uploadFile() throws IOException {
        cloudinary.url();
        return "a";
    }

    public void updateImage(String publicId) throws Exception {
        cloudinary.api().update(publicId,
                ObjectUtils.asMap(
                        "tags", "important",
                        "moderation_status", "approved"));
    }

    public String getImageUrl(String publicId) {
        try {
            return cloudinary.url().version(cloudinary.api().resource(publicId, null).get("version")).generate(publicId);
        } catch (Exception e) {
            throw new CustomException(ErrorConstant.NOT_FOUND + publicId);
        }
    }

    public String getThumbnailUrl(String publicId) {
        try {
            String quality = "auto:low";

            Transformation transformation = new Transformation().quality(quality).width(300).height(200);

            return cloudinary.url().transformation(transformation)
                    .version(cloudinary.api().resource(publicId, null).get("version"))
                    .generate(publicId);
        } catch (Exception e) {
            throw new CustomException(ErrorConstant.NOT_FOUND + publicId);
        }
    }

    public HashMap<String, String> uploadFileToFolder(String pathName, String fileName, byte[] imageData) throws IOException {
        var file = cloudinary.uploader()
                .upload(imageData,
                        Map.of(
                                PUBLIC_ID, fileName,
                                UPLOAD_PRESET, pathName,
                                OVERWRITE, true
                        ));

        return (HashMap<String, String>) file;
    }

    public void deleteImage(String publicId) throws IOException {
        // Xóa hình ảnh từ Cloudinary bằng cách sử dụng public ID
        Map<String, String> options = ObjectUtils.asMap("invalidate", true);
        cloudinary.uploader().destroy(publicId, options);
    }

}

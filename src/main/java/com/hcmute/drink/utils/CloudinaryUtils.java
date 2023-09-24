package com.hcmute.drink.utils;

import com.cloudinary.Cloudinary;
import com.hcmute.drink.constant.CloudinaryConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.hcmute.drink.constant.CloudinaryConstant.*;

@Component
@RequiredArgsConstructor
public class CloudinaryUtils {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of(
                                PUBLIC_ID, UUID.randomUUID().toString(),
                                UPLOAD_PRESET, PRODUCT_PATH
                        ))
                .get(URL_PROPERTY)
                .toString();
    }
    public String uploadFileToFolder(String pathName, String fileName, MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of(
                                PUBLIC_ID, fileName,
                                UPLOAD_PRESET, pathName,
                                OVERWRITE, true
                        ))
                .get(URL_PROPERTY)
                .toString();
    }
}

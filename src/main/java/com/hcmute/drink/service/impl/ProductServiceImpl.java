package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.repository.ProductRepository;
import com.hcmute.drink.service.ProductService;
import com.hcmute.drink.utils.CloudinaryUtils;
import com.hcmute.drink.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.hcmute.drink.constant.ErrorConstant.NOT_FOUND;
import static com.hcmute.drink.constant.ErrorConstant.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudinaryUtils cloudinaryUtils;
    private final CategoryServiceImpl categoryService;
    private final ModelMapper modelMapper;
    private final ImageUtils imageUtils;

    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;
    public ProductCollection exceptionIfNotExistedProductById(String id) throws Exception {

        ProductCollection product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new Exception(PRODUCT_NOT_FOUND + " with product id " + id);
        }
        return product;
    }

    // SERVICE =================================================================

    public ProductCollection createProduct(ProductCollection data, MultipartFile image) throws Exception {
        Date currentDate = new Date();
        long currentTimeMillis = currentDate.getTime();

        byte[] originalImage = image.getBytes();
        byte[] newImage = imageUtils.resizeImage(originalImage, 200, 200);
        byte[] newThumbnail = imageUtils.resizeImage(originalImage, 200, 200);

        categoryService.exceptionIfNotExistedCategoryById(data.getCategoryId().toString());

        HashMap<String, String> imageUploaded = cloudinaryUtils.uploadFileToFolder(
                CloudinaryConstant.PRODUCT_PATH,
                data.getName() + "_" + currentTimeMillis,
                newImage
        );
        ImageEmbedded imageEmbedded = new ImageEmbedded(imageUploaded.get(CloudinaryConstant.PUBLIC_ID), imageUploaded.get(CloudinaryConstant.URL_PROPERTY));
        data.setImage(imageEmbedded);

        HashMap<String, String> thumbUploaded = cloudinaryUtils.uploadFileToFolder(
                CloudinaryConstant.PRODUCT_PATH,
                data.getName() + "_thumb_" + currentTimeMillis ,
                newThumbnail
        );
        ImageEmbedded thumbEmbedded = new ImageEmbedded(thumbUploaded.get(CloudinaryConstant.PUBLIC_ID), thumbUploaded.get(CloudinaryConstant.URL_PROPERTY));
        data.setThumbnail(thumbEmbedded);

        ProductCollection product = productRepository.save(data);
        if (product != null) {
            return product;
        }
        throw new Exception(ErrorConstant.CREATED_FAILED);
    }

    public GetProductByIdResponse getProductDetailsById(String id) throws Exception {
        GetProductByIdResponse product = productRepository.getProductDetailsById(id);
        if(product == null) {
            throw new Exception(PRODUCT_NOT_FOUND);
        }
        return product;
    }

    public GetProductEnabledByIdResponse getProductEnabledById(String id) throws Exception {
        GetProductEnabledByIdResponse product = productRepository.getProductEnabledById(id);
        if(product == null) {
            throw new Exception(NOT_FOUND);
        }
        return product;
    }
    public List<GetProductsByCategoryIdResponse> getProductsByCategoryId(String categoryId) throws Exception {
        return productRepository.getProductsByCategoryId(new ObjectId(categoryId));
    }

    public List<GetAllProductsEnabledResponse> getAllProductsEnabled() throws Exception {
        return productRepository.getAllProductsEnabled();
    }

    public List<GetAllProductsResponse> getAllProducts() throws Exception {
        return productRepository.getAllProducts();
    }

    public boolean deleteProductById(String id) throws Exception {
        ProductCollection product = exceptionIfNotExistedProductById(id);
        ImageEmbedded image = product.getImage();
        cloudinaryUtils.deleteImage(image.getId());
        productRepository.deleteById(id);
        return true;
    }


    public ProductCollection updateProductById(UpdateProductRequest data, String id) throws Exception {
        ProductCollection product = productRepository.findById(id).orElse(null);

        if(product == null) {
            throw new Exception(NOT_FOUND);
        }

        modelMapper.map(data, product);

        if(data.getImage() != null) {
            ImageEmbedded oldImage = product.getImage();
            ImageEmbedded oldThumbnail = product.getThumbnail();

            cloudinaryUtils.deleteImage(oldImage.getId());
            cloudinaryUtils.deleteImage(oldThumbnail.getId());

            byte[] originalImage = data.getImage().getBytes();

            byte[] newImage = imageUtils.resizeImage(originalImage, 200, 200);
            HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.PRODUCT_PATH, data.getName(), newImage);
            ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
            product.setImage(imageEmbedded);

            byte[] thumbnailImage = imageUtils.resizeImage(originalImage, 200, 200);
            HashMap<String, String> thumbnailUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.PRODUCT_PATH, data.getName(), thumbnailImage);
            ImageEmbedded thumbnailEmbedded = new ImageEmbedded(thumbnailUploaded.get(CloudinaryConstant.PUBLIC_ID), thumbnailUploaded.get(CloudinaryConstant.URL_PROPERTY));
            product.setThumbnail(thumbnailEmbedded);
        }

        return productRepository.save(product);
    }
}

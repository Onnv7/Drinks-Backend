package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.CreateProductRequest;
import com.hcmute.drink.repository.ProductRepository;
import com.hcmute.drink.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {
    private final ProductRepository productRepository;
    private final CloudinaryUtils cloudinaryUtils;
    private final ModelMapper modelMapper;

    public ProductCollection createProduct(ProductCollection data, List<MultipartFile> images) throws Exception {
        int size = images.size();
        List<ImageModel> imagesList = new ArrayList<ImageModel>();
        for (int i = 0; i < size; i++) {
            HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(
                    CloudinaryConstant.PRODUCT_PATH,
                    data.getName() + (i + 1),
                    images.get(i)
            );
            imagesList.add(new ImageModel(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY)));
        }
        data.setImagesList(imagesList);
        ProductCollection product = productRepository.save(data);
        if (product != null) {
            return product;
        }
        throw new Exception(ErrorConstant.CREATED_FAILED);
    }

    public ProductCollection getProductById(String id) throws Exception {
        ProductCollection product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        return product;
    }

    public List<ProductCollection> getAllProducts() throws Exception {
        return productRepository.findAll();
    }

    public boolean deleteProductById(String id) throws Exception {
        ProductCollection product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        List<ImageModel> images = product.getImagesList();
        int size = images.size();
        for (int i = 0; i < size; i++) {
            cloudinaryUtils.deleteImage(images.get(i).getId());
        }
        productRepository.deleteById(id);
        return true;
    }

    public ProductCollection updateProductById(ProductCollection data, List<MultipartFile> images) throws Exception {
        ProductCollection product = productRepository.findById(data.getId()).orElse(null);

        if(product == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        List<ImageModel> imagesList = new ArrayList<ImageModel>();
//        ProductCollection newProductCol = product.clone();
        // FIXME: chỗ update cretedAt bị null khi update: https://stackoverflow.com/questions/35584271/spring-data-mongodb-annotation-createddate-isnt-working-when-id-is-assigned-m
        int size = images.size();
        List<ImageModel> oldImages = product.getImagesList();
        modelMapper.map(data, product);
        for (int i = 0; i < size; i++) {
            cloudinaryUtils.deleteImage(oldImages.get(i).getId());
            HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.PRODUCT_PATH, data.getName(), images.get(i));
            imagesList.add(new ImageModel(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY)));
        }
        product.setImagesList(imagesList);
        ProductCollection newProduct = productRepository.save(product);
        if(newProduct != null) {
            return newProduct;
        }
        throw new Exception(ErrorConstant.UPDATE_FAILED);
    }
}

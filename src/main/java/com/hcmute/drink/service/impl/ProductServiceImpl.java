package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.GetAllProductsResponse;
import com.hcmute.drink.dto.GetProductsByCategoryIdResponse;
import com.hcmute.drink.repository.ProductRepository;
import com.hcmute.drink.service.ProductService;
import com.hcmute.drink.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudinaryUtils cloudinaryUtils;
    private final CategoryServiceImpl categoryService;
    private final ModelMapper modelMapper;
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public ProductCollection createProduct(ProductCollection data, List<MultipartFile> images) throws Exception {
        int size = images.size();
        categoryService.exceptionIfNotFoundById(data.getCategoryId().toString());

        List<ImageEmbedded> imagesList = new ArrayList<ImageEmbedded>();
        for (int i = 0; i < size; i++) {
            HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(
                    CloudinaryConstant.PRODUCT_PATH,
                    data.getName() + (i + 1),
                    images.get(i)
            );
            imagesList.add(new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY)));
        }

        data.setImageList(imagesList);
        ProductCollection product = productRepository.save(data);
        if (product != null) {
            return product;
        }
        throw new Exception(ErrorConstant.CREATED_FAILED);
    }

    public ProductCollection findProductById(String id) throws Exception {
        ProductCollection product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        return product;
    }
    public List<GetProductsByCategoryIdResponse> getProductsByCategoryId(String categoryId) throws Exception {
        return productRepository.getProductsByCategoryId(new ObjectId(categoryId));
    }

    public List<GetAllProductsResponse> getAllProducts() throws Exception {
        return productRepository.getAllProducts();
    }

    public boolean deleteProductById(String id) throws Exception {
        ProductCollection product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        List<ImageEmbedded> images = product.getImageList();
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

        List<ImageEmbedded> imagesList = new ArrayList<ImageEmbedded>();
        modelMapperNotNull.map(data, product);
        int size = images.size();

        List<ImageEmbedded> oldImages = product.getImageList();

        for (int i = 0; i < size; i++) {
            cloudinaryUtils.deleteImage(oldImages.get(i).getId());
            HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.PRODUCT_PATH, data.getName(), images.get(i));
            imagesList.add(new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY)));
        }
        product.setImageList(imagesList);
        ProductCollection newProduct = productRepository.save(product);
        if(newProduct != null) {
            return newProduct;
        }
        throw new Exception(ErrorConstant.UPDATE_FAILED);
    }
}

package com.hcmute.drink.service.implement;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateProductRequest;
import com.hcmute.drink.dto.request.UpdateProductRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.ProductRepository;
import com.hcmute.drink.service.IProductService;
import com.hcmute.drink.utils.CloudinaryUtils;
import com.hcmute.drink.utils.ImageUtils;
import com.hcmute.drink.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.hcmute.drink.constant.ErrorConstant.NOT_FOUND;
import static com.hcmute.drink.constant.ErrorConstant.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CloudinaryUtils cloudinaryUtils;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ImageUtils imageUtils;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    @Lazy
    private OrderService orderService;


    public ProductCollection getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    // SERVICE =================================================================

    @Override
    public void createProduct(CreateProductRequest body, MultipartFile image) {

        ProductCollection data = modelMapperUtils.mapClass(body, ProductCollection.class);
        Date currentDate = new Date();
        long currentTimeMillis = currentDate.getTime();

        byte[] originalImage = new byte[0];
        try {
            originalImage = image.getBytes();
            byte[] newImage = imageUtils.resizeImage(originalImage, 200, 200);
            byte[] newThumbnail = imageUtils.resizeImage(originalImage, 200, 200);

            categoryService.getById(data.getCategoryId().toString());

            HashMap<String, String> imageUploaded = cloudinaryUtils.uploadFileToFolder(
                    CloudinaryConstant.PRODUCT_PATH,
                    data.getName() + "_" + currentTimeMillis,
                    newImage
            );
            ImageEmbedded imageEmbedded = new ImageEmbedded(imageUploaded.get(CloudinaryConstant.PUBLIC_ID), imageUploaded.get(CloudinaryConstant.URL_PROPERTY));
            data.setImage(imageEmbedded);

            HashMap<String, String> thumbUploaded = cloudinaryUtils.uploadFileToFolder(
                    CloudinaryConstant.PRODUCT_PATH,
                    data.getName() + "_thumb_" + currentTimeMillis,
                    newThumbnail
            );

            ImageEmbedded thumbEmbedded = new ImageEmbedded(thumbUploaded.get(CloudinaryConstant.PUBLIC_ID), thumbUploaded.get(CloudinaryConstant.URL_PROPERTY));
            data.setThumbnail(thumbEmbedded);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        productRepository.save(data);
    }

    @Override
    public GetProductByIdResponse getProductDetailsById(String id) {
        GetProductByIdResponse product = productRepository.getProductDetailsById(id);
        if (product == null) {
            throw new CustomException(PRODUCT_NOT_FOUND);
        }
        return product;
    }

    @Override
    public GetProductEnabledByIdResponse getProductEnabledById(String id) {
        GetProductEnabledByIdResponse product = productRepository.getProductEnabledById(id);
        if (product == null) {
            throw new CustomException(NOT_FOUND + id);
        }
        return product;
    }

    @Override
    public List<GetProductsByCategoryIdResponse> getProductsByCategoryId(String categoryId) {
        return productRepository.getProductsByCategoryId(new ObjectId(categoryId));
    }

    @Override
    public List<GetAllProductsEnabledResponse> getAllProductsEnabled(int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return productRepository.getAllProductsEnabled(skip, limit);
    }

    @Override
    public List<GetAllProductsResponse> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public void deleteProductById(String id) {
        ProductCollection product = getById(id);
        ImageEmbedded image = product.getImage();
        try {
            cloudinaryUtils.deleteImage(image.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productRepository.deleteById(id);
    }

    @Override
    public void updateProductById(UpdateProductRequest data, String id) {
        ProductCollection product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new CustomException(NOT_FOUND);
        }

        modelMapper.map(data, product);

        if (data.getImage() != null) {
            ImageEmbedded oldImage = product.getImage();
            ImageEmbedded oldThumbnail = product.getThumbnail();

            try {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        productRepository.save(product);
    }

    @Override
    public List<GetAllProductsEnabledResponse> searchProductByNameOrDescription(String key, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return productRepository.searchProductByNameOrDescription(key, skip, limit);
    }

    @Override
    public List<GetAllProductsEnabledResponse> getTopProductQuantityOrder(int quantity)  {
        List<GetAllProductsEnabledResponse> resData = orderService.getTopProductQuantityOrder(quantity);
        if (resData.size() < quantity) {
            Iterator itr = resData.iterator();
            List<ObjectId> excludeId = new ArrayList<>();
            while (itr.hasNext()) {
                GetAllProductsEnabledResponse product = (GetAllProductsEnabledResponse) itr.next();
                excludeId.add(new ObjectId(product.getId()));
            }
            resData.addAll(productRepository.getSomeProduct(quantity - resData.size(), excludeId));
        }
        return resData;
    }
}

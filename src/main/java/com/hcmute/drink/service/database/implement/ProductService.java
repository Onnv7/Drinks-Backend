package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.collection.embedded.SizeEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateProductRequest;
import com.hcmute.drink.dto.request.UpdateProductRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.ProductStatus;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.ProductRepository;
import com.hcmute.drink.service.database.IProductService;
import com.hcmute.drink.service.elasticsearch.ProductSearchService;
import com.hcmute.drink.utils.CloudinaryUtils;
import com.hcmute.drink.utils.ImageUtils;
import com.hcmute.drink.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final ImageUtils imageUtils;
    private final ModelMapperUtils modelMapperUtils;
    private final SequenceService sequenceService;
    private final ProductSearchService productSearchService;
    @Autowired
    @Lazy
    private OrderService orderService;


    public ProductCollection getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    public static double getMinPrice(List<SizeEmbedded> sizeList) {
        double min = sizeList.get(0).getPrice();
        for (SizeEmbedded item : sizeList) {
            if(min > item.getPrice()) {
                min = item.getPrice();
            }
        }
        return min;
    }

    // SERVICE =================================================================

    @Transactional
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
        data.setStatus(ProductStatus.HIDDEN);
        data.setCode(sequenceService.generateCode(ProductCollection.SEQUENCE_NAME, ProductCollection.PREFIX_CODE, ProductCollection.LENGTH_NUMBER));
        ProductCollection dataSaved = productRepository.save(data);
        productSearchService.createProduct(dataSaved);
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
    public List<GetAllVisibleProductResponse> getAllProductsVisible(int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return productRepository.getAllProductsEnabled(skip, limit);
    }

    @Override
    public List<GetAllProductsResponse> getAllProducts(int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return productRepository.getAllProducts(skip, limit);
    }

    @Transactional
    @Override
    public void deleteProductById(String id) {
        ProductCollection product = getById(id);
        ImageEmbedded image = product.getImage();

        productRepository.deleteById(id);
        productSearchService.deleteProduct(id);

        try {
            cloudinaryUtils.deleteImage(image.getId());
        } catch (IOException e) {
            productSearchService.createProduct(product);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void updateProductById(UpdateProductRequest data, String id) {
        ProductCollection product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new CustomException(NOT_FOUND);
        }

        modelMapperUtils.map(data, product);

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

        productSearchService.upsertProduct(product);
    }

    @Override
    public List<GetAllVisibleProductResponse> searchProductVisible(String key, int page, int size) {
        return modelMapperUtils.mapList(productSearchService.searchVisibleProduct(key, page, size), GetAllVisibleProductResponse.class);
    }
    @Override
    public List<GetAllProductsResponse> searchProduct(String key, int page, int size) {
        return modelMapperUtils.mapList(productSearchService.searchProduct(key, page, size), GetAllProductsResponse.class);
    }
    @Override
    public List<GetAllVisibleProductResponse> getTopProductQuantityOrder(int quantity)  {
        List<GetAllVisibleProductResponse> resData = orderService.getTopProductQuantityOrder(quantity);
        if (resData.size() < quantity) {
            Iterator itr = resData.iterator();
            List<ObjectId> excludeId = new ArrayList<>();
            while (itr.hasNext()) {
                GetAllVisibleProductResponse product = (GetAllVisibleProductResponse) itr.next();
                excludeId.add(new ObjectId(product.getId()));
            }
            resData.addAll(productRepository.getSomeProduct(quantity - resData.size(), excludeId));
        }
        return resData;
    }
}

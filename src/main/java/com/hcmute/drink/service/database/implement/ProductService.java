package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.CategoryCollection;
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
import com.hcmute.drink.model.elasticsearch.ProductIndex;
import com.hcmute.drink.repository.database.ProductRepository;
import com.hcmute.drink.service.database.IProductService;
import com.hcmute.drink.service.elasticsearch.ProductSearchService;
import com.hcmute.drink.service.common.CloudinaryService;
import com.hcmute.drink.utils.ImageUtils;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.utils.RegexUtils;
import com.hcmute.drink.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.hcmute.drink.constant.ErrorConstant.*;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final CategoryService categoryService;
    private final ModelMapperService modelMapperService;
    private final SequenceService sequenceService;
    private final ProductSearchService productSearchService;
    @Autowired
    @Lazy
    private OrderService orderService;


    public ProductCollection getById(String id) {
        return productRepository.getById(id)
                .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    public ProductCollection saveProduct(ProductCollection data) {
        return productRepository.save(data);
    }

    public static long getMinPrice(List<SizeEmbedded> sizeList) {
        long min = sizeList.get(0).getPrice();
        for (SizeEmbedded item : sizeList) {
            if (min > item.getPrice()) {
                min = item.getPrice();
            }
        }
        return min;
    }

    // SERVICE =================================================================

    @Transactional
    @Override
    public void createProduct(CreateProductRequest body, MultipartFile image) {

        ProductCollection data = modelMapperService.mapClass(body, ProductCollection.class);
        Date currentDate = new Date();

        byte[] originalImage = new byte[0];
        try {
            originalImage = image.getBytes();
            byte[] newImage = ImageUtils.resizeImage(originalImage, 200, 200);
            byte[] newThumbnail = ImageUtils.resizeImage(originalImage, 200, 200);

            CategoryCollection categoryCollection = categoryService.getById(data.getCategoryId().toString());
            if (categoryCollection.isCanDelete()) {
                categoryCollection.setCanDelete(false);
                categoryService.saveCategory(categoryCollection);
            }

            HashMap<String, String> imageUploaded = cloudinaryService.uploadFileToFolder(
                    CloudinaryConstant.PRODUCT_PATH,
                    StringUtils.generateFileName(body.getName(), "product"),
                    newImage
            );
            ImageEmbedded imageEmbedded = new ImageEmbedded(imageUploaded.get(CloudinaryConstant.PUBLIC_ID), imageUploaded.get(CloudinaryConstant.URL_PROPERTY));
            data.setImage(imageEmbedded);

            HashMap<String, String> thumbUploaded = cloudinaryService.uploadFileToFolder(
                    CloudinaryConstant.PRODUCT_PATH,
                    StringUtils.generateFileName(body.getName(), "product_thumb"),
                    newThumbnail
            );

            ImageEmbedded thumbEmbedded = new ImageEmbedded(thumbUploaded.get(CloudinaryConstant.PUBLIC_ID), thumbUploaded.get(CloudinaryConstant.URL_PROPERTY));
            data.setThumbnail(thumbEmbedded);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        data.setStatus(ProductStatus.HIDDEN);
        data.setCode(sequenceService.generateCode(ProductCollection.SEQUENCE_NAME, ProductCollection.PREFIX_CODE, ProductCollection.LENGTH_NUMBER));
        data.setPrice(getMinPrice(data.getSizeList()));
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
    public List<GetAllVisibleProductResponse> getAllProductsVisible(int page, int size, String key) {
        int skip = (page - 1) * size;
        int limit = size;
        if (key != null) {
            return modelMapperService.mapList(productSearchService.searchVisibleProduct(key, page, size), GetAllVisibleProductResponse.class);
        }
        return productRepository.getAllProductsEnabled(skip, limit);
    }

    @Override
    public GetProductListResponse getProductList(String key, int page, int size, String categoryId, ProductStatus productStatus) {
        int skip = (page - 1) * size;
        int limit = size;
        String categoryIdRegex = RegexUtils.generateFilterRegexString(categoryId != null ? categoryId : "");
        String productStatusRegex = RegexUtils.generateFilterRegexString(productStatus != null ? productStatus.toString() : "");
        if (key == null) {
            return productRepository.getProductList(skip, limit, categoryIdRegex, productStatusRegex);
        } else {
            Page<ProductIndex> productPage = productSearchService.searchProduct(key, categoryIdRegex, productStatusRegex, page, size);
            GetProductListResponse resultPage = new GetProductListResponse();
            resultPage.setTotalPage(productPage.getTotalPages());
            resultPage.setProductList(modelMapperService.mapList(productPage.getContent(), GetProductListResponse.Product.class));
            return resultPage;
        }
    }

    @Transactional
    @Override
    public void deleteProductById(String id) {
        ProductCollection product = getById(id);
        if (product.isCanDelete()) {
            product.setDeleted(true);
            productRepository.save(product);
            productSearchService.deleteProduct(id);
        } else {
            throw new CustomException(CANT_DELETE);
        }
    }

    // TODO: kiểm tra lại trường hợp có 1 id không thể xóa
    @Override
    public DeleteSomeProductResponse deleteSomeProductById(List<String> productIdList) {
        int successCount = 0;
        for (String id : productIdList) {
            try {
                deleteProductById(id);
                successCount++;
            } catch (Exception e) {

            }
        }
        return DeleteSomeProductResponse.builder().successCount(successCount).build();
    }

    @Transactional
    @Override
    public void updateProductById(UpdateProductRequest body, String id) {
        ProductCollection product = productRepository.getById(id).orElse(null);

        if (product == null) {
            throw new CustomException(NOT_FOUND);
        }

        modelMapperService.map(body, product);

        if (body.getImage() != null) {
            ImageEmbedded oldImage = product.getImage();
            ImageEmbedded oldThumbnail = product.getThumbnail();

            try {
                cloudinaryService.deleteImage(oldImage.getId());
                cloudinaryService.deleteImage(oldThumbnail.getId());

                byte[] originalImage = body.getImage().getBytes();

                byte[] newImage = ImageUtils.resizeImage(originalImage, 200, 200);
                HashMap<String, String> fileUploaded = cloudinaryService.uploadFileToFolder(CloudinaryConstant.PRODUCT_PATH,
                        StringUtils.generateFileName(body.getName(), "product"), newImage);
                ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
                product.setImage(imageEmbedded);

                byte[] thumbnailImage = ImageUtils.resizeImage(originalImage, 200, 200);
                HashMap<String, String> thumbnailUploaded = cloudinaryService.uploadFileToFolder(CloudinaryConstant.PRODUCT_PATH,
                        StringUtils.generateFileName(body.getName(), "product"), thumbnailImage);
                ImageEmbedded thumbnailEmbedded = new ImageEmbedded(thumbnailUploaded.get(CloudinaryConstant.PUBLIC_ID), thumbnailUploaded.get(CloudinaryConstant.URL_PROPERTY));
                product.setThumbnail(thumbnailEmbedded);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        product.setPrice(getMinPrice(product.getSizeList()));

        productRepository.save(product);
        productSearchService.upsertProduct(product);
    }


    @Override
    public List<GetAllVisibleProductResponse> getTopProductQuantityOrder(int quantity) {
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

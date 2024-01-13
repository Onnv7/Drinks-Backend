package com.hcmute.drink.controller;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.model.elasticsearch.ProductIndex;
import com.hcmute.drink.repository.database.OrderRepository;
import com.hcmute.drink.repository.database.ProductRepository;
import com.hcmute.drink.repository.elasticsearch.OrderSearchRepository;
import com.hcmute.drink.repository.elasticsearch.ProductSearchRepository;
import com.hcmute.drink.service.elasticsearch.OrderSearchService;
import com.hcmute.drink.service.elasticsearch.ProductSearchService;
import com.hcmute.drink.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tool")
@Tag(name = "tool")
@RequiredArgsConstructor
public class ToolController {
    private final ProductSearchRepository productSearchRepository;
    private final OrderSearchService orderSearchService;
    private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;
    private final OrderSearchRepository orderSearchRepository;
    private final OrderRepository orderRepository;
    private final ModelMapperUtils modelMapperUtils;

    @GetMapping("/sync-product")
    public String addElasticSearch() {
        productSearchRepository.deleteAll();
        List<ProductCollection> productList = productRepository.findAll();
        for (ProductCollection item : productList) {
            productSearchService.createProduct(item);
        }
        return "okokok";
    }
    @GetMapping("/sync-order")
    public String syncOrder() {
        orderSearchRepository.deleteAll();
        List<OrderCollection> productList = orderRepository.findAll();
        for (OrderCollection item : productList) {
            orderSearchService.createOrder(item);
        }
        return "okokok";
    }

    @GetMapping("/product")
    public List<ProductIndex> searchProduct(
            @Parameter(name = "key", description = "Key is order's id, customer name or phone number", required = false, example = "65439a55e9818f43f8b8e02c")
            @RequestParam("key") String key) {
        Pageable page = PageRequest.of(0, 3);
//        List<ProductIndex> lst = productSearchRepository.searchVisibleProduct(key, page).getContent();
//        return lst;
        return null;
    }
}

package com.hcmute.drink.controller;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.model.elasticsearch.ProductIndex;
import com.hcmute.drink.repository.database.OrderRepository;
import com.hcmute.drink.repository.database.ProductRepository;
import com.hcmute.drink.repository.elasticsearch.OrderSearchRepository;
import com.hcmute.drink.repository.elasticsearch.ProductSearchRepository;
import com.hcmute.drink.service.common.CloudinaryService;
import com.hcmute.drink.service.elasticsearch.OrderSearchService;
import com.hcmute.drink.service.elasticsearch.ProductSearchService;
import com.hcmute.drink.service.common.ModelMapperService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private final ModelMapperService modelMapperService;
    private final CloudinaryService cloudinaryService;
    private String extractBoundary(HttpServletRequest request) {
        String boundaryHeader = "boundary=";
        int i = request.getContentType().indexOf(boundaryHeader) +
                boundaryHeader.length();
        return request.getContentType().substring(i);
    }

    @ResponseBody
    @RequestMapping(path = "/fileupload", method = RequestMethod.POST, consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void fileUpload(HttpServletRequest request) throws IOException {
        Files.copy(request.getInputStream(), Paths.get("./myfilename"));
    }
    @PostMapping(value = "/stream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Flux<String> streamData(@RequestBody Flux<Part> parts) {
        return parts
                .flatMap(part -> part.content().map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return new String(bytes);
                }));
    }
    @PostMapping(value = "/uploadAndStream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> uploadAndStreamFiles(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        return Flux.create((FluxSink<String> sink) -> {
            try (InputStream inputStream = file.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    sink.next(line); // Send each line to Flux
                }

                sink.complete(); // When reading is complete
            } catch (IOException e) {
                sink.error(e); // In case of an error
            }
        }).delayElements(Duration.ofSeconds(1));
    }
    @GetMapping(value = "/stream-time", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamTime() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> LocalTime.now().toString());
    }


    @ResponseBody
    @GetMapping(path = "/fileupload1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String fileUpload1() throws IOException {
        return cloudinaryService.uploadFile();
    }

    @ResponseBody
    @GetMapping(path = "/fileupload2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> fileUpload2() throws IOException, InterruptedException {
        System.out.println("1");
        Mono<String> res = Mono.just("Hello, Mono!").delayElement(Duration.ofSeconds(3));
        System.out.println("2");
        return res;
    }

    @ResponseBody
    @GetMapping(path = "/fileupload3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String fileUpload3() throws IOException, InterruptedException {
        System.out.println("1");
        Mono<String> res = Mono.just("Hello, Mono!").delayElement(Duration.ofSeconds(3));
        System.out.println("2");
        return "res";
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleStream(@RequestParam("file") MultipartFile file) {
        // Process the input stream as needed
        try {

            InputStream inputStream = file.getInputStream();
            int data;
            while ((data = inputStream.read()) != -1) {
                System.out.print((char) data);
                Thread.sleep(0);
            }
            byte[] fileBytes = file.getBytes();

            // In ra dữ liệu của tệp tin (chú ý: đây là một cách in đơn giản, không phải là tệp tin văn bản)
            System.out.println(new String(fileBytes));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the input stream");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("Data streamed successfully");
    }

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

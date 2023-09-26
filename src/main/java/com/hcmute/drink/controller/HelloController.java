package com.hcmute.drink.controller;

import com.hcmute.drink.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class HelloController {
    private final CloudinaryUtils cloudinaryUtils;
    @GetMapping()
    private String greeting() {
        return "Hello";
    }
    @GetMapping("/ok")
    private String ok() {
        return "Veridied";
    }
//
//    @GetMapping("/secured")
//    public String secured(@AuthenticationPrincipal UserPrincipal principal) {
//
//        return "secured " + principal.getEmail() + " - " + principal.getUserId();
//    }
//
//    @GetMapping("/admin")
//    public String admin(@AuthenticationPrincipal UserPrincipal principal) {
//        return "admin " + principal.getEmail() + " - " + principal.getUserId();
//    }

    @PostMapping("/upload")
    public String upload(@RequestParam("image")MultipartFile multipartFile) {
        try{
            String imageUrl = cloudinaryUtils.uploadFile(multipartFile);
            return imageUrl;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

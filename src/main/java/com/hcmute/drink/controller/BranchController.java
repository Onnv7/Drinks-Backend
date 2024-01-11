package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.request.CreateAddressRequest;
import com.hcmute.drink.dto.request.CreateBranchRequest;
import com.hcmute.drink.dto.request.UpdateBranchRequest;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.implement.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;


@Tag(name = BRANCH_CONTROLLER_TITLE)
@RestController
@RequestMapping(BRANCH_BASE_PATH)
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;
    @Operation(summary = BRANCH_CREATE)
    @PostMapping(path = POST_BRANCH_CREATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> createBranch(@RequestBody @Valid CreateBranchRequest body) {
        branchService.createBranch(body);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.CREATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.CREATED);
    }

    @Operation(summary = BRANCH_UPDATE_BY_ID)
    @PostMapping(path = PUT_BRANCH_UPDATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateBranchInfoById(@RequestBody @Valid UpdateBranchRequest body, @PathVariable(BRANCH_ID) String branchId) {
        branchService.updateBranch(body, branchId);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.CREATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.CREATED);
    }
}

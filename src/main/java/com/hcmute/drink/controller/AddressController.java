package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.request.CreateAddressRequest;
import com.hcmute.drink.dto.request.UpdateAddressRequest;
import com.hcmute.drink.dto.response.GetAddressByUserIdResponse;
import com.hcmute.drink.dto.response.GetAddressDetailsByIdResponse;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.IAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = ADDRESS_CONTROLLER_TITLE)
@RestController
@RequestMapping(ADDRESS_BASE_PATH)
@RequiredArgsConstructor
public class AddressController {
    private final IAddressService addressService;

    @Operation(summary = ADDRESS_ADD_ADDRESS_BY_ID_SUM)
    @PostMapping(path = POST_ADDRESS_CREATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> addAddressToUserByUserId(
            @PathVariable(USER_ID) String userId,
            @RequestBody @Valid CreateAddressRequest body
    ) {
        addressService.createAddressToUser(body, userId);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.CREATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.CREATED);
    }

    @Operation(summary = ADDRESS_UPDATE_ADDRESS_BY_ID_SUM)
    @PutMapping(path = PUT_ADDRESS_UPDATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateAddressById(
            @PathVariable(ADDRESS_ID) String addressId,
            @RequestBody @Validated UpdateAddressRequest body
    ) {
        addressService.updateAddressById(body, addressId);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.UPDATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = ADDRESS_DELETE_ADDRESS_BY_ID_SUM)
    @DeleteMapping(path = DELETE_ADDRESS_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> deleteAddressById(@PathVariable(ADDRESS_ID) String addressId) {
        addressService.deleteAddressById(addressId);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.DELETED)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = ADDRESS_GET_BY_USER_ID_SUM)
    @GetMapping(path = GET_ADDRESS_BY_USER_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAddressByUserId(@PathVariable(USER_ID) String userId) {

        List<GetAddressByUserIdResponse> resData = addressService.getAddressByUserId(userId);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = ADDRESS_GET_DETAILS_BY_ID_SUM)
    @GetMapping(path = GET_ADDRESS_DETAILS_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAddressDetailsById(@PathVariable(ADDRESS_ID) String addressId) {
        GetAddressDetailsByIdResponse resData = addressService.getAddressDetailsById(addressId);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

}

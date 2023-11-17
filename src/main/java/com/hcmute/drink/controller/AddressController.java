package com.hcmute.drink.controller;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.AddressService;
import com.hcmute.drink.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
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
    private final AddressService addressService;
    private final SecurityUtils securityUtils;
    private final ModelMapper modelMapper;

    @Operation(summary = ADDRESS_ADD_ADDRESS_BY_ID_SUM, description = ADDRESS_ADD_ADDRESS_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = ADDRESS_CREATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> addAddressToUserByUserId(@PathVariable("userId") String userId,
                                                                @RequestBody @Validated CreateAddressRequest body
    ) {
        try {
            AddressCollection data = modelMapper.map(body, AddressCollection.class);
            data.setUserId(new ObjectId(userId));
            AddressCollection createdData = addressService.createAddressToUser(data, userId);
//            CreateAddressResponse resData = modelMapper.map(createdData, CreateAddressResponse.class);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.CREATED)
//                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ADDRESS_UPDATE_ADDRESS_BY_ID_SUM, description = ADDRESS_UPDATE_ADDRESS_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PutMapping(path = ADDRESS_UPDATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateAddressById(@PathVariable("addressId") String addressId,
                                                         @RequestBody @Validated UpdateAddressRequest body
    ) {
        try {
//            securityUtils.exceptionIfNotMe(userId);
            AddressCollection data = modelMapper.map(body, AddressCollection.class);
            data.setId(addressId);
            AddressCollection updatedData = addressService.updateAddressById(data);
            UpdateAddressResponse resData = modelMapper.map(updatedData, UpdateAddressResponse.class);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.UPDATED)
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ADDRESS_DELETE_ADDRESS_BY_ID_SUM, description = ADDRESS_DELETE_ADDRESS_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.DELETED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @DeleteMapping(path = ADDRESS_DELETE_SUB_PATH)
    public ResponseEntity<ResponseAPI> deleteAddressById(@PathVariable("addressId") String addressId) {
        try {
            addressService.deleteAddressById(addressId);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.DELETED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ADDRESS_GET_BY_USER_ID_SUM, description = ADDRESS_GET_BY_USER_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ADDRESS_GET_BY_USER_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAddressByUserId(@PathVariable("userId") String userId) {
        try {
            List<GetAddressByUserIdResponse> resData= addressService.getAddressByUserId(userId);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resData)
                    .message(SuccessConstant.GET)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ADDRESS_GET_DETAILS_BY_ID_SUM, description = ADDRESS_GET_DETAILS_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ADDRESS_GET_DETAILS_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAddressDetailsById(@PathVariable("addressId") String addressId) {
        try {
            GetAddressDetailsByIdResponse resData= addressService.getAddressById(addressId);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resData)
                    .message(SuccessConstant.GET)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

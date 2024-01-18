package com.hcmute.drink.service.database;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.dto.request.CreateAddressRequest;
import com.hcmute.drink.dto.response.GetAddressListByUserIdResponse;
import com.hcmute.drink.dto.response.GetAddressDetailsByIdResponse;
import com.hcmute.drink.dto.request.UpdateAddressRequest;

import java.util.List;

public interface IAddressService {

    AddressCollection createAddressToUser(CreateAddressRequest body, String userId);
    void updateAddressById(UpdateAddressRequest body, String addressId);
    List<GetAddressListByUserIdResponse> getAddressListByUserId(String userId);
    void deleteAddressById(String addressId);
    GetAddressDetailsByIdResponse getAddressDetailsById(String id);
}

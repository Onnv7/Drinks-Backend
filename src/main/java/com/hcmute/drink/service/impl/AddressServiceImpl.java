package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.dto.GetAddressByUserIdResponse;
import com.hcmute.drink.dto.GetAddressDetailsByIdResponse;
import com.hcmute.drink.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hcmute.drink.constant.ErrorConstant.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl {
    private final UserServiceImpl userService;
    private final AddressRepository addressRepository;
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public AddressCollection createAddressToUser(AddressCollection data, String userId) throws Exception {
        userService.exceptionIfNotExistedUserById(userId);
        List<GetAddressByUserIdResponse> addresses = addressRepository.getAddressByUserId(new ObjectId(userId));
        if (addresses.size() == 0) {
            data.setDefault(true);
        } else {
            data.setDefault(false);
        }
        return addressRepository.save(data);
    }

    public AddressCollection updateAddressById(String addressId, AddressCollection data) throws Exception {
        AddressCollection address = addressRepository.findById(addressId).orElse(null);
        if (address == null) {
            throw new Exception(NOT_FOUND + addressId);
        }
        modelMapperNotNull.map(data, address);
        return addressRepository.save(address);
    }

    public void deleteAddressById(String addressId) throws Exception {
        addressRepository.deleteById(addressId);
    }

    public List<GetAddressByUserIdResponse> getAddressByUserId(String userId) throws Exception {
        userService.exceptionIfNotExistedUserById(userId);
        return addressRepository.getAddressByUserId(new ObjectId(userId));
    }

    public GetAddressDetailsByIdResponse getAddressById(String id) throws Exception {
        return addressRepository.getAddressDetailsById(id);
    }

    public void setDefaultAddress(String id) throws Exception {
        AddressCollection address = addressRepository.findById(id).orElseThrow();
        ObjectId userId = address.getUserId();
        List<AddressCollection> addresses = addressRepository.findByUserId(userId);
        for (AddressCollection add : addresses) {
            add.setDefault(false);
            addressRepository.save(add);
        }
        address.setDefault(true);
        addressRepository.save(address);
    }
}

package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.GetAddressByUserIdResponse;
import com.hcmute.drink.dto.GetAddressDetailsByIdResponse;
import com.hcmute.drink.repository.AddressRepository;
import com.hcmute.drink.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
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
    private final SecurityUtils securityUtils;

    public AddressCollection createAddressToUser(AddressCollection data, String userId) throws Exception {
        securityUtils.exceptionIfNotMe(userId);
        userService.exceptionIfNotExistedUserById(userId);
        List<GetAddressByUserIdResponse> addresses = addressRepository.getAddressByUserId(new ObjectId(userId));
        if(addresses.size() >= 5) {
            throw new Exception(ErrorConstant.OVER_FIVE_ADDRESS);
        }
        if (addresses.size() == 0) {
            data.setDefault(true);
        } else {
            data.setDefault(false);
        }
        return addressRepository.save(data);
    }

    public AddressCollection updateAddressById(AddressCollection data) throws Exception {
        AddressCollection address = addressRepository.findById(data.getId()).orElse(null);
        if (address == null) {
            throw new Exception(NOT_FOUND + data.getId());
        }
        if (data.isDefault()){
            setDefaultAddress(data.getId());
        }
        modelMapperNotNull.map(data, address);
        return addressRepository.save(address);
    }

    public void deleteAddressById(String addressId) throws Exception {
        addressRepository.deleteById(addressId);
    }

    public List<GetAddressByUserIdResponse> getAddressByUserId(String userId) throws Exception {
        securityUtils.exceptionIfNotMe(userId);
        userService.exceptionIfNotExistedUserById(userId);
        List<GetAddressByUserIdResponse> list = addressRepository.getAddressByUserId(new ObjectId(userId));
        Collections.sort(list, new Comparator<GetAddressByUserIdResponse>() {
            @Override
            public int compare(GetAddressByUserIdResponse address1, GetAddressByUserIdResponse address2) {
                if (address1.isDefault() && !address2.isDefault()) {
                    return -1;
                } else if (!address1.isDefault() && address2.isDefault()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return list;
    }

    public GetAddressDetailsByIdResponse getAddressById(String id) throws Exception {
        return addressRepository.getAddressDetailsById(id);
    }

    public void setDefaultAddress(String addressId) throws Exception {
        AddressCollection address = addressRepository.findById(addressId).orElseThrow();
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

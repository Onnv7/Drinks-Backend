package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.ErrorConstant;
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
        UserCollection user = userService.exceptionIfNotExistedUserById(userId);
        List<ObjectId> addressList = user.getAddressIds();
        if(addressList.size() == 3) {
            throw new Exception(ErrorConstant.THREE_ADDRESS);
        }

        AddressCollection address = addressRepository.save(data);
        user.getAddressIds().add(new ObjectId(address.getId()));
        userService.updateUser(userId, user);
        return address;
    }

    public AddressCollection updateAddressById(String addressId, AddressCollection data) throws Exception {
        AddressCollection address = addressRepository.findById(addressId).orElse(null);
        if(address == null) {
            throw new Exception(NOT_FOUND + addressId);
        }
        modelMapperNotNull.map(data, address);
        return addressRepository.save(address);
    }

    public void deleteAddressById(String addressId) throws Exception {
        addressRepository.deleteById(addressId);
    }
}

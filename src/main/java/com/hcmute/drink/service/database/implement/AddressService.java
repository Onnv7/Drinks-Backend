package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.database.GetUserIdFromAddressDto;
import com.hcmute.drink.dto.request.*;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.AddressRepository;
import com.hcmute.drink.service.database.IAddressService;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.hcmute.drink.constant.ErrorConstant.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;
    private final ModelMapperService modelMapperService;

    public AddressCollection getAddressById(String id) {
        return addressRepository.findById(id).orElseThrow(() -> new CustomException(NOT_FOUND + id));
    }
    private void checkUserIdFromAddressId(String addressId) {
        GetUserIdFromAddressDto user = addressRepository.getUserIdFromAddressId(addressId).orElseThrow(() -> new CustomException(NOT_FOUND + " user of address"));
        SecurityUtils.checkUserId(user.getUserId());
    }
    @Override
    public AddressCollection createAddressToUser(CreateAddressRequest body, String userId)  {
        SecurityUtils.checkUserId(userId);
        AddressCollection data = modelMapperService.mapClass(body, AddressCollection.class);
        data.setUserId(new ObjectId(userId));
        List<GetAddressListByUserIdResponse> addresses = addressRepository.getAddressListByUserId(new ObjectId(userId));
        if(addresses.size() >= 5) {
            throw new CustomException(ErrorConstant.OVER_FIVE_ADDRESS);
        }
        data.setDefault(addresses.isEmpty());
        return addressRepository.save(data);
    }

    @Override
    public void updateAddressById(UpdateAddressRequest body, String addressId) {
        AddressCollection data = modelMapperService.mapClass(body, AddressCollection.class);
        AddressCollection address = getAddressById(addressId);
        checkUserIdFromAddressId(addressId);
        if (data.isDefault()){
            ObjectId userId = address.getUserId();
            List<AddressCollection> addresses = addressRepository.findByUserId(userId);
            for (AddressCollection add : addresses) {
                add.setDefault(false);
                addressRepository.save(add);
            }
            address.setDefault(true);
            addressRepository.save(address);
        }
        modelMapperService.mapNotNull(data, address);
        addressRepository.save(address);
    }

    @Override
    public void deleteAddressById(String addressId)  {
        if(addressRepository.existsById(addressId)) {
            checkUserIdFromAddressId(addressId);
            addressRepository.deleteById(addressId);
            return;
        }
        throw new CustomException(NOT_FOUND + addressId);
    }


    @Override
    public List<GetAddressListByUserIdResponse> getAddressListByUserId(String userId)  {
        SecurityUtils.checkUserId(userId);

        List<GetAddressListByUserIdResponse> list = addressRepository.getAddressListByUserId(new ObjectId(userId));
        Collections.sort(list, new Comparator<GetAddressListByUserIdResponse>() {
            @Override
            public int compare(GetAddressListByUserIdResponse address1, GetAddressListByUserIdResponse address2) {
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

    @Override
    public GetAddressDetailsByIdResponse getAddressDetailsById(String addressId)  {
        if(addressRepository.existsById(addressId)) {
            checkUserIdFromAddressId(addressId);
            return addressRepository.getAddressDetailsById(addressId);
        }
        throw new CustomException(NOT_FOUND + addressId);
    }

}

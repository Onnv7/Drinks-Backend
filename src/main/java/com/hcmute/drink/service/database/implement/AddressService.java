package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.*;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.AddressRepository;
import com.hcmute.drink.service.database.IAddressService;
import com.hcmute.drink.utils.ModelMapperUtils;
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
    private final ModelMapperUtils modelMapperUtils;

    @Override
    public AddressCollection createAddressToUser(CreateAddressRequest body, String userId)  {
        AddressCollection data = modelMapperUtils.mapClass(body, AddressCollection.class);
        List<GetAddressByUserIdResponse> addresses = addressRepository.getAddressByUserId(new ObjectId(userId));
        if(addresses.size() >= 5) {
            throw new CustomException(ErrorConstant.OVER_FIVE_ADDRESS);
        }
        if (addresses.size() == 0) {
            data.setDefault(true);
        } else {
            data.setDefault(false);
        }
        return addressRepository.save(data);
    }

    @Override
    public void updateAddressById(UpdateAddressRequest body, String addressId) {
        AddressCollection data = modelMapperUtils.mapClass(body, AddressCollection.class);
        AddressCollection address = addressRepository.findById(addressId).orElse(null);
        if (address == null) {
            throw new CustomException(NOT_FOUND + addressId);
        }
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
        modelMapperUtils.mapNotNull(data, address);
        addressRepository.save(address);
    }

    @Override
    public void deleteAddressById(String addressId)  {
        addressRepository.deleteById(addressId);
    }


    @Override
    public List<GetAddressByUserIdResponse> getAddressByUserId(String userId)  {

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

    @Override
    public GetAddressDetailsByIdResponse getAddressDetailsById(String id)  {
        return addressRepository.getAddressDetailsById(id);
    }

}

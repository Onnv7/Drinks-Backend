package com.hcmute.drink.service;

import com.hcmute.drink.collection.ConfirmationCollection;
import com.hcmute.drink.repository.ConfirmationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.hcmute.drink.constant.ErrorConstant.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ConfirmationService {
    private final ConfirmationRepository confirmationRepository;
    private final ModelMapper modelMapper;
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;
    public void createOrUpdateConfirmationInfo(String email, String code) {
        ConfirmationCollection oldConfirmation = confirmationRepository.findByEmail(email);
        if(oldConfirmation == null) {
            ConfirmationCollection confirmation = ConfirmationCollection.builder()
                    .email(email)
                    .code(code)
                    .build();
            confirmationRepository.save(confirmation);
        }
        else {
            oldConfirmation.setCode(code);
            confirmationRepository.save(oldConfirmation);
        }
    }

    public void updateConfirmationInfo(ConfirmationCollection confirmation) throws Exception {
        ConfirmationCollection data = confirmationRepository.findById(confirmation.getId()).orElse(null);
        if(data == null) {
            throw new Exception(NOT_FOUND);
        }
        modelMapperNotNull.map(confirmation, data);
        confirmationRepository.save(data);
    }
}

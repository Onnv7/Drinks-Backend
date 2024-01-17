package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.ConfirmationCollection;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.ConfirmationRepository;
import com.hcmute.drink.service.database.IConfirmationService;
import com.hcmute.drink.service.common.ModelMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hcmute.drink.constant.ErrorConstant.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ConfirmationService implements IConfirmationService {
    private final ConfirmationRepository confirmationRepository;
    private final ModelMapperService modelMapperService;
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

    public void updateConfirmationInfo(ConfirmationCollection confirmation) {
        ConfirmationCollection data = confirmationRepository.findById(confirmation.getId()).orElse(null);
        if(data == null) {
            throw new CustomException(NOT_FOUND + confirmation.getId());
        }
        modelMapperService.mapNotNull(confirmation, data);
        confirmationRepository.save(data);
    }
}

package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.SequenceCollection;
import com.hcmute.drink.repository.database.SequenceRepository;
import com.hcmute.drink.utils.GeneratorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SequenceService {
    private final SequenceRepository sequenceRepository;

    @Transactional
    public String generateCode(String sequenceName, String prefix, int length) {
        SequenceCollection sequence = sequenceRepository.findById(sequenceName).orElse(null);
        long count = 1;
        if(sequence == null) {
            sequence = SequenceCollection.builder()
                    .count(1)
                    .id(sequenceName).build();
        } else {
            count = sequence.getCount() + 1;
            sequence.setCount(count);
        }
        sequenceRepository.save(sequence);
//        throw new CustomException("ádlkasnd");
        return GeneratorUtils.formatCodeItem(count, prefix, length);
    }
}

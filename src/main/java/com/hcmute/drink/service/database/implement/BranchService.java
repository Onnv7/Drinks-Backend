package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.BranchCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateBranchRequest;
import com.hcmute.drink.dto.request.UpdateBranchRequest;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.BranchRepository;
import com.hcmute.drink.service.database.IBranchService;
import com.hcmute.drink.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BranchService implements IBranchService {
    private final BranchRepository branchRepository;
    private final SequenceService sequenceService;
    private final ModelMapperUtils modelMapperUtils;
    public void createBranch(CreateBranchRequest body) {
        BranchCollection branch = modelMapperUtils.mapClass(body, BranchCollection.class);
        branch.setCode(sequenceService.generateCode(BranchCollection.SEQUENCE_NAME, BranchCollection.PREFIX_CODE, BranchCollection.LENGTH_NUMBER));
        branchRepository.save(branch);
    }

    public void updateBranch(UpdateBranchRequest body, String id) {
        BranchCollection branch = branchRepository.findById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
        modelMapperUtils.map(body, branch);
        branchRepository.save(branch);
    }
}

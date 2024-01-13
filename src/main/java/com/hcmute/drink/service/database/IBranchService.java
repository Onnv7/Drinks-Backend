package com.hcmute.drink.service.database;

import com.hcmute.drink.collection.BranchCollection;
import com.hcmute.drink.dto.request.CreateBranchRequest;
import com.hcmute.drink.dto.request.UpdateBranchRequest;

import java.util.List;

public interface IBranchService {
    void deleteBranchById(String id);
    void updateBranchById(UpdateBranchRequest body, String id);
    void createBranch(CreateBranchRequest body);
    List<BranchCollection> getBranchList();
}

package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.enums.ConditionOperator;
import com.hcmute.drink.enums.ConditionType;
import lombok.Builder;
import lombok.Data;

@Data
public class CouponConditionEmbedded {
    private String description;
    private ConditionType type;
    private Object value;
}

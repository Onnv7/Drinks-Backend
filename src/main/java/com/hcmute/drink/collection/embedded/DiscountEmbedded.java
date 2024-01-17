package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.enums.DiscountUnitType;
import lombok.Data;

@Data
public class DiscountEmbedded {
    private DiscountUnitType unit;
    private Long value;
}

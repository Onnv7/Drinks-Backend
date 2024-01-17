package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.enums.DiscountUnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyDiscountEmbedded {
    private DiscountUnitType unit;
    private Long value;
}

package com.hcmute.drink.collection;


import com.hcmute.drink.collection.embedded.CouponConditionEmbedded;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.collection.embedded.MoneyDiscountEmbedded;
import com.hcmute.drink.collection.embedded.ProductGiftEmbedded;
import com.hcmute.drink.enums.CouponStatus;
import com.hcmute.drink.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "coupon")
public class CouponCollection {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private CouponType couponType;
    private String description;
    private MoneyDiscountEmbedded moneyDiscount;
    private ProductGiftEmbedded productGift;

    private Integer quantity;
    private CouponStatus status;

    private List<CouponConditionEmbedded> conditionList;
    private Date startDate;
    private Date expirationDate;
    private boolean canMultiple;
    private boolean isDeleted;

}

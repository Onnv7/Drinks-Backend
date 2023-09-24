package com.hcmute.drink.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hcmute.drink.constant.StatusCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Date timestamp;
    @Builder.Default
    private boolean success = true;
    private String message;
    private T data;

}

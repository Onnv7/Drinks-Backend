package com.hcmute.drink.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {
    @Builder.Default
    private Date timestamp = new Date();
    private boolean success;
    private String message;
    private T details;
    private String stack;
}

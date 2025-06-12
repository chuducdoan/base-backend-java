package com.example.myshop.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseResponse implements Serializable {

    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public static BaseResponse of(Object data) {
        return of(ResponseCode.SUCCESS, data);
    }

    public static BaseResponse of(IResponseCode responseCode) {
        return of(responseCode, null);
    }

    public static BaseResponse of(IResponseCode responseCode, Object data) {
        return  of(responseCode.getCode(), responseCode.getMessage(), data);
    }

    public static  BaseResponse of(String code, String message, Object data) {
        return BaseResponse.builder().code(code).message(message).data(data).build();
    }
}

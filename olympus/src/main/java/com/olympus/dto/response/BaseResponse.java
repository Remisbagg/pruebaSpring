package com.olympus.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Schema
@NoArgsConstructor
public class BaseResponse <T,E>{
    @Schema(example = "200")
    private int code;
    @Schema(example = "success")
    private String status;
    @Schema(example = "message")
    private String message;
    @Schema(example = "[\"item1\", \"item2\"]")
    private T data;
    @Schema(example = "[]")
    private E error;

    public BaseResponse(HttpStatus httpStatus, String status, String message, T data, E error) {
        this.code = httpStatus.value();
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public static <T> BaseResponse<T,?> success (HttpStatus httpStatus, String status, String  message, T data) {
        return new BaseResponse<>(httpStatus,status, message, data, null);
    }

    public static <E> BaseResponse<E,?> error (HttpStatus httpStatus, String status, String  message, E error) {
        return new BaseResponse<>(httpStatus,status, message, null, error);
    }
}

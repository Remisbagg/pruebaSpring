package com.olympus.exception;

import lombok.Getter;

@Getter
public class ReportCreateException extends RuntimeException{
    private final String detail;

    public ReportCreateException(String detail) {
        super(detail);
        this.detail = detail;
    }
}

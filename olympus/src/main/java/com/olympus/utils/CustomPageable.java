package com.olympus.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomPageable {
    int pageNumber;
    int pageSize;
    int totalPages;
    long totalElements;
    boolean first;
    boolean last;
}

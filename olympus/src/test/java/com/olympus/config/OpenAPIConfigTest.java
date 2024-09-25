package com.olympus.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@ExtendWith(MockitoExtension.class)
class OpenAPIConfigTest {
    @InjectMocks
    OpenAPIConfig openAPIConfig;

    @Test
    void testOctetStreamJsonConverterBean() {
        var converter = openAPIConfig.octetStreamJsonConverter();
        assertNotNull(converter);
    }

    @Test
    void testMyOpenAPIBean() {
        var openAPI = openAPIConfig.myOpenAPI();
        assertNotNull(openAPI);
    }
}
package com.alfa.api.sdk.client.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpHeaders {
    public static final String ACCEPT = "Accept";

    @UtilityClass
    public static class Accept {
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_XML = "application/xml";
        public static final String TEXT_PLAIN = "text/plain";
    }
}

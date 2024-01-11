package com.alfa.api.sdk.client.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    public static boolean isSuccessfulStatusCode(int status) {
        return status >= 200 && status <= 399;
    }
}

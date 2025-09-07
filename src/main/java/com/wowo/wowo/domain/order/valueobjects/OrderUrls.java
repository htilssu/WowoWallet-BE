package com.wowo.wowo.domain.order.valueobjects;

import lombok.Value;

/**
 * Order URLs value object
 * Contains redirect URLs for payment flow
 */
@Value
public class OrderUrls {
    String returnUrl;
    String successUrl;
    
    public OrderUrls(String returnUrl, String successUrl) {
        validateUrl(returnUrl, "Return URL");
        validateUrl(successUrl, "Success URL");
        
        this.returnUrl = returnUrl;
        this.successUrl = successUrl;
    }
    
    private void validateUrl(String url, String urlType) {
        if (url != null && url.length() > 300) {
            throw new IllegalArgumentException(urlType + " cannot exceed 300 characters");
        }
    }
}
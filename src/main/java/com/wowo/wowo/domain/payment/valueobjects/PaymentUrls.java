package com.wowo.wowo.domain.payment.valueobjects;

/**
 * Payment URLs value object for return and success callbacks
 */
public record PaymentUrls(String returnUrl, String successUrl) {
    
    public static PaymentUrls of(String returnUrl, String successUrl) {
        return new PaymentUrls(returnUrl, successUrl);
    }
    
    public boolean hasReturnUrl() {
        return returnUrl != null && !returnUrl.trim().isEmpty();
    }
    
    public boolean hasSuccessUrl() {
        return successUrl != null && !successUrl.trim().isEmpty();
    }
}
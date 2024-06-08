package com.swdgr6.bikeplatform.model.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


public abstract class PaymentResDTO {
    @Builder
    @Data
    @AllArgsConstructor
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}

package com.neero.ticketing_service.enums;

public enum PaymentMethod {
    NEERO_WALLET("NEERO_WALLET", "Neero Wallet"),
    CREDIT_CARD("CREDIT_CARD", "Credit Card"),
    DEBIT_CARD("DEBIT_CARD", "Debit Card"),
    MOBILE_MONEY("MOBILE_MONEY", "Mobile Money"),
    BANK_TRANSFER("BANK_TRANSFER", "Bank Transfer");
    
    private final String code;
    private final String displayName;
    
    PaymentMethod(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

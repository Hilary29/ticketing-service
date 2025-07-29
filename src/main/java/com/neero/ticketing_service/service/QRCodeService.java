package com.neero.ticketing_service.service;

public interface QRCodeService {

    String generateQRCode(String data);

    String generateQRCodeImage(String data, int width, int height);
    
    byte[] generateQRCodeImage(String data);

    boolean validateQRCode(String qrCode);

    String decodeQRCode(String qrCodeImage);
}


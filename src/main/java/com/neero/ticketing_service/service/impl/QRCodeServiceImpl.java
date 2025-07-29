package com.neero.ticketing_service.service.impl;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.neero.ticketing_service.service.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class QRCodeServiceImpl implements QRCodeService {

    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 350;

    @Override
    public String generateQRCode(String data) {
        return generateQRCodeImage(data, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public String generateQRCodeImage(String data, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            byte[] qrCodeBytes = outputStream.toByteArray();
            String base64QRCode = Base64.getEncoder().encodeToString(qrCodeBytes);

            log.debug("QR code generated successfully for data: {}", data);
            return "data:image/png;base64," + base64QRCode;

        } catch (Exception e) {
            log.error("Error generating QR code: {}", e.getMessage());
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    @Override
    public boolean validateQRCode(String qrCode) {
        try {
            // Basic validation - check if it's a valid base64 string
            if (qrCode == null || !qrCode.startsWith("data:image/png;base64,")) {
                return false;
            }

            String base64Data = qrCode.substring("data:image/png;base64,".length());
            Base64.getDecoder().decode(base64Data);

            return true;
        } catch (Exception e) {
            log.error("Invalid QR code: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public byte[] generateQRCodeImage(String data) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            log.debug("QR code byte array generated for data: {}", data);
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error generating QR code byte array: {}", e.getMessage());
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    @Override
    public String decodeQRCode(String qrCodeImage) {
        try {
            String base64Data = qrCodeImage.substring("data:image/png;base64,".length());
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();

        } catch (Exception e) {
            log.error("Error decoding QR code: {}", e.getMessage());
            throw new RuntimeException("Failed to decode QR code", e);
        }
    }
}


package it.twentyfive.demoqrcode.controller;

import it.twentyfive.demoqrcode.model.ResponseImage;
import it.twentyfive.demoqrcode.utils.MethodUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.imageio.ImageIO;

@RestController
public class QrCodeController {

    public static final int DEFAULT_QR_WIDTH = 350;
    public static final int DEFAULT_QR_HEIGHT = 350;

    @PostMapping("/generate")
public ResponseEntity<ResponseImage> downloadQrCodeBase64(@RequestParam String requestUrl) {
    try {
        byte[] qrCodeBytes = MethodUtils.generateQrCodeImage(requestUrl, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT, Color.BLACK, Color.YELLOW);
        BufferedImage qrCodeImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));
        
        // Leggi i byte del file del logo
        String imagePath = "img/scan-me.png";
        BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));
        
        // Aggiungi il logo all'immagine con il testo
        BufferedImage finalImage = MethodUtils.addLogoToBorder(qrCodeImage, logo, 0.6);
        
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(finalImage, "PNG", pngOutputStream);
        String base64 = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
        base64 = "data:image/png;base64," + base64;
        
        ResponseImage response = new ResponseImage();
        response.setImageBase64(base64);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}


}

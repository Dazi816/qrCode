package it.twentyfive.demoqrcode.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MethodUtils {

    public static byte[] generateQrCodeImage(String text, int width, int height, Color qrCodeColor, Color backgroundColor) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageConfig con = new MatrixToImageConfig(qrCodeColor.getRGB(), backgroundColor.getRGB());
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
        // Calcola le dimensioni del riquadro bianco al centro
        int whiteBoxSize = (int) (Math.min(width, height) * 0.135); // Riduci la dimensione del riquadro bianco
        int whiteBoxX = (width - whiteBoxSize) / 2;
        int whiteBoxY = (height - whiteBoxSize) / 2;

        // Imposta il riquadro bianco trasparente
        BufferedImage overlayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = overlayImage.createGraphics();
        graphics.setColor(new Color(255, 255, 255, 0)); // Trasparente
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(whiteBoxX, whiteBoxY, whiteBoxSize, whiteBoxSize);
        graphics.dispose();

        // Sovrappone l'immagine al codice QR
        Graphics2D qrGraphics = image.createGraphics();
        qrGraphics.drawImage(overlayImage, 0, 0, null);
        qrGraphics.dispose();
        BufferedImage imageWithBorder = addBorder(image, 20, Color.BLUE);
        addTextToBorder(imageWithBorder, "SCAN ME", Color.WHITE, 20, 15);
        String imagePath = "C:\\Users\\dazi9\\OneDrive\\Desktop\\qr code\\demo-qrcode\\src\\main\\resources\\img\\logo.png";
        BufferedImage logo = ImageIO.read(new File(imagePath));
        logo = resizeImage(logo, whiteBoxSize, whiteBoxSize);
        BufferedImage imageWithLogo =addLogoToCenter(imageWithBorder, logo);
        
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageWithLogo, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }
    public static BufferedImage addLogoToCenter(BufferedImage baseImage, BufferedImage logo) {
        int logoX = (baseImage.getWidth() - logo.getWidth()) / 2;
        int logoY = (baseImage.getHeight() - logo.getHeight()) / 2;

        BufferedImage imageWithLogo = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageWithLogo.createGraphics();
        g.drawImage(baseImage, 0, 0, null);
        g.drawImage(logo, logoX, logoY, null);
        g.dispose();

        return imageWithLogo;
    }

    public static BufferedImage addBorder(BufferedImage img, int borderSize, Color borderColor) {

        BufferedImage imgWithBorder = new BufferedImage(img.getWidth() + 2*borderSize, img.getHeight() +  2*borderSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imgWithBorder.createGraphics();
        g.setColor(borderColor);
        g.fillRect(0, 0, imgWithBorder.getWidth(), imgWithBorder.getHeight());
        g.drawImage(img, borderSize, borderSize, null);
        g.dispose();
    
        return imgWithBorder;
    }
    public static void addTextToBorder(BufferedImage img, String text, Color textColor, int x, int y) {
        Graphics2D g = img.createGraphics();
        g.setColor(textColor);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(text, x, y);
        g.dispose();
    }
    public static BufferedImage addLogoToBorder(BufferedImage img, BufferedImage logo, double scaleFactor) {
        int logoWidth = (int) (logo.getWidth() * scaleFactor);
        int logoHeight = (int) (logo.getHeight() * scaleFactor);
    
        int logoX = 0;
        int logoY = (img.getHeight() - logoHeight);
    
        BufferedImage scaledLogo = resizeImage(logo, logoWidth, logoHeight);
    
        BufferedImage imageWithLogo = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageWithLogo.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.drawImage(scaledLogo, logoX, logoY, null);
        g.dispose();
    
        return imageWithLogo;
    }
    
    
}

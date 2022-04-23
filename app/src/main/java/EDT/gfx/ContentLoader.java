package EDT.gfx;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ContentLoader {

    public static BufferedImage loadImageFromUrl(String url) {
        try {
            URL urlInstance = new URL(url);
            return ImageIO.read(urlInstance);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static BufferedImage loadImage(String path){
        try {
            return ImageIO.read(ContentLoader.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static Icon loadImageGif(String path) {
        try {
            return new ImageIcon(ContentLoader.class.getResourceAsStream(path).readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static InputStream loadAudio(String path) {
        try {
            return new BufferedInputStream(ContentLoader.class.getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static Font loadFont(String path, float size){
        try {
            return Font.createFont(Font.TRUETYPE_FONT, ContentLoader.loadAudio(path)).deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }


}
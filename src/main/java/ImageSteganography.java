


import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ImageSteganography {
    private final BufferedImage image;
    private String message;

    public ImageSteganography(InputStream is) throws IOException {
        image = ImageIO.read(is);
    }

    public ImageSteganography(File file) throws IOException {
        image = ImageIO.read(file);
    }

    public ImageSteganography(URL url) throws IOException {
        image = ImageIO.read(url);
    }

    public ImageSteganography(ImageInputStream imageInputStream) throws IOException {
        image = ImageIO.read(imageInputStream);
    }

    public ImageSteganography(BufferedImage image){
        this.image = image;
    }

    public ImageSteganography(InputStream is,String message) throws IOException {
        image = ImageIO.read(is);
        this.message = message;
    }

    public ImageSteganography(File file,String message) throws IOException {
        image = ImageIO.read(file);
        this.message = message;
    }

    public ImageSteganography(URL url,String message) throws IOException {
        image = ImageIO.read(url);
        this.message = message;
    }

    public ImageSteganography(ImageInputStream imageInputStream,String message) throws IOException {
        image = ImageIO.read(imageInputStream);
        this.message = message;
    }

    public ImageSteganography(BufferedImage image,String message){
        this.image = image;
        this.message = message;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private byte[] getRGBBytes(int x, int y){
        if(x > this.image.getWidth() || y > this.image.getHeight()){
            throw new IndexOutOfBoundsException();
        }
        short[] rgbValues = {
                (short) ((this.image.getRGB(x,y) & 0xff0000) >> 16),
                (short) ((this.image.getRGB(x,y) & 0xff00) >> 8),
                (short) (this.image.getRGB(x, y) & 0xff)
        };
        byte[] rgbBytes = new byte[3];
        for(int i = 0; i < 3; i++){
            rgbBytes[i] = (byte) rgbValues[i];
        }
        return rgbBytes;
    }
    private byte[] getRGBBytes() {
        List<Byte> bytes = new ArrayList<>();
        System.out.println(image.getHeight() + " " + image.getWidth());
        for (int y = 0; y < this.image.getHeight(); y++) {
            for (int x = 0; x < this.image.getWidth(); x++) {
                byte[] temp = this.getRGBBytes(x, y);
                for(byte t : temp){
                    bytes.add(t);
                }
            }
        }

        byte[] out = new byte[bytes.size()];
        for (int i = 0; i < bytes.size();i++) {
            out[i] = bytes.get(i);
        }
        return out;
    }

    public BufferedImage hideText(String message) throws IOException {
        this.message = message;
        return hideText();
    }

    public BufferedImage hideText() throws IOException {
        setRGBbytes();
        return image;
    }

    private void setRGBbytes(){
        byte[] newRGBBytes = Steganography.hideData(getRGBBytes(),message.getBytes(StandardCharsets.UTF_8));
        int[] newRGBValues = new int[newRGBBytes.length/3];//1 RGB Value -> 3 shorts -> 6 byte
        for (int i = 0; i < newRGBBytes.length; i+=3){

            int r = newRGBBytes[i] & 0xFF;
            int g = newRGBBytes[i + 1] & 0xFF;
            int b = newRGBBytes[i + 2] & 0xFF;

            newRGBValues[i/3] = new Color(r,g,b).getRGB();
        }
        for (int y = 0; y < this.image.getHeight(); y++) {
            for (int x = 0; x < this.image.getWidth(); x++) {
                this.image.setRGB(x,y,newRGBValues[(y* image.getWidth())+x]);
            }
        }
    }

    public byte[] getText(){
        return Steganography.getData(this.getRGBBytes());
    }
    public static void main(String[] args) throws IOException {
        BufferedImage bi = ImageIO.read(ImageSteganography.class.getClassLoader().getResourceAsStream("LilliaProfilePic.jpg"));

        ImageSteganography imageSteganography = new ImageSteganography(bi,"This is a hidden message. PLS don't find ö");
        ImageIO.write(imageSteganography.hideText(),"png",new File("src/main/resources/LilliaProfilePic2.png"));

        ImageSteganography getMessage = new ImageSteganography(ImageIO.read(ImageSteganography.class.getResourceAsStream("LilliaProfilePic2.png")));
        System.out.println(new String((getMessage.getText())));
    }
}

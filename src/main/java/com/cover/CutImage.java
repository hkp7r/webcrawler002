package com.cover;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

public class CutImage {
    public static void main(String[] args) throws Exception {

        File file = new File("D:\\BT1024\\▲tj1221▲最強高清有碼中文合集[1216]");

        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                loopFolder(f);
            }
        }
    }

    private static void loopFolder(File file) throws Exception {
        int imgCnt = 0;
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                continue;
            }
            String fPath = f.getAbsolutePath().toString();
            if (fPath.endsWith(".jpg") || fPath.endsWith(".jpeg")) {
                imgCnt++;
            }
        }

        if (imgCnt > 1) {
            return;
        }


        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                continue;
            }
            System.out.println(f.getAbsolutePath().toString());
            String fPath = f.getAbsolutePath().toString();
            if (!fPath.endsWith(".jpg") && !fPath.endsWith(".jpeg")) {
                continue;
            }
            String newNamePath = fPath.substring(0, fPath.length() - 4) + "_cover.jpeg";
            System.out.println(newNamePath);
            f.renameTo(new File(newNamePath));
            try {
                cutJpeg(newNamePath, fPath);
            } catch (Exception e) {

            } finally {
                continue;
            }
        }
    }

    private static void cutJpeg(String jpgPath, String output) throws Exception {

        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(jpgPath));
        FileOutputStream out = new FileOutputStream(output);

        int oldH = bufferedImage.getHeight();
        int oldW = bufferedImage.getWidth();

        int y = 0;
        int x = oldW / 2 + (int) (oldW * 0.027);


        InputStream input = new FileInputStream(jpgPath);
        ImageInputStream imageStream = null;
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();

            System.out.println(reader.getWidth(0));
            System.out.println(reader.getHeight(0));
            Rectangle rect = new Rectangle(x, y, oldW / 2, oldH);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "jpg", out);
        } finally {
            imageStream.close();
        }


    }
}

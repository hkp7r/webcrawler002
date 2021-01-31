package com.cover;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CreatePosterImg {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\BT1024\\１１１１１１１");

        for (File file1 : file.listFiles()) {
            if (file1.isDirectory()) {
                for (File file2 : file1.listFiles()) {
//                    System.out.println(file2.getName());
//                    System.out.println(file2.getAbsolutePath());
                    if (file2.getAbsolutePath().contains("-poster.jpg")) {
                        System.out.println(file2.getAbsolutePath());
                        System.out.println(file2.getName());
                        String newFile = file2.getParent();
                        String name[] = file2.getName().split("\\.");
                        System.out.println(file2.getParent() + "\\" + "poster.jpg");
                        FileUtils.copyFile(file2, new File(file2.getParent() + "\\" + "poster.jpg"));
                    }
                }
            }
        }
    }
}

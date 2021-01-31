package com.xfile;

import java.io.*;

public class Rename {
    public static void main(String[] args) throws IOException {

        String nameListFile = "C:\\Download\\BT1024\\亞洲無碼\\img.txt";
        String imgPath = "C:\\迅雷下载\\";

        FileReader fr = new FileReader(new File(nameListFile));
        BufferedReader br = new BufferedReader(fr);

        String line;
        String lineArry[];
        while ((line = br.readLine()) != null) {
            lineArry = line.split("   ");
            String[] tmp = lineArry[0].split("/");
            String taget = tmp[tmp.length - 1];
            System.out.println(taget);
            System.out.println(lineArry[1].toString());
            (new File(imgPath+taget)).renameTo(new File(imgPath + lineArry[1].toString()));

        }
        br.close();
        fr.close();

    }
}

package com.folder;

import org.jsoup.internal.StringUtil;

import java.io.File;
import java.util.SortedMap;

public class EditFolderName {
    public static void main(String[] args) {
        String rootFolderPath = "D:\\BT1024\\▲老含及▲有碼中文合集[12.29]";
        File rootFolder = new File(rootFolderPath);

        for (File folder : rootFolder.listFiles()) {
            if (folder.isDirectory()) {
                try {
                    editName1(folder);
                } catch (Exception e) {
                    System.out.println(e.toString());
                } finally {
                    continue;
                }

            }
        }


    }

    private static void editName1(File folder) throws Exception {

        System.out.println(folder.getName());// WAAA009C
        System.out.println(folder.getAbsoluteFile());// \\synology\BT下载\待处理\WAAA009C
        System.out.println(folder.getCanonicalPath());// \\synology\BT下载\待处理\WAAA009C
        System.out.println(folder.getPath());// \\synology\BT下载\待处理\WAAA009C
        System.out.println(folder.getParent());// \\synology\BT下载\待处理
        System.out.println(folder.toString());// \\synology\BT下载\待处理\WAAA009C

        String name = folder.getName().toUpperCase();

        if (name.endsWith("_CH_SD") ||
                name.endsWith("_HD_CH") ||
                name.endsWith("_FHD_CH") ||
                name.endsWith("-MP4-FHD") ||
                name.endsWith("MP4")) {
            String newFolderName = name.replace("_CH_SD", "")
                    .replace("_HD_CH", "")
                    .replace("-MP4-FHD", "")
                    .replace("MP4", "");

            File newFolder = new File(folder.getParent() + File.separator + newFolderName);
            folder.renameTo(newFolder);
            //System.out.println("変更後フォルダ名：" + folder.getParent() + File.separator + newFolderName);
            editName2(newFolder);
        } else {
            editName2(folder);
        }
    }

    private static void editName2(File folder) throws Exception {

        String name = folder.getName().toUpperCase();

        if (StringUtil.isNumeric(name.substring(0, 3))) {
            name = name.substring(3);
        }
        if (name.endsWith("C") && Character.isDigit(name.substring(name.length() - 2, name.length() - 1).charAt(0))) {
            System.out.println(name.substring(name.length() - 2, name.length() - 1).charAt(0));
            System.out.println(name.substring(0, name.length() - 1));

            String tName = name.substring(0, name.length() - 1);
            char[] tNameCH = tName.toCharArray();
            for (int i = tNameCH.length - 1; i >= 0; i--) {
                if (Character.isDigit(tNameCH[i])) {
                    System.out.println(tNameCH[i]);
                    continue;
                } else {
                    System.out.println(tNameCH[i]);
                    String newFolderName = tName.substring(0, i + 1) + "-" + tName.substring(i + 1, tName.length());
                    System.out.println(newFolderName);
                    File newFolder = new File(folder.getParent() + File.separator + newFolderName.toUpperCase());
                    folder.renameTo(newFolder);
                    break;
                }
            }

        } else {
            char[] tNameCH = name.toCharArray();
            boolean numFlg = false;
            for (int i = tNameCH.length - 1; i >= 0; i--) {
                if (Character.isDigit(tNameCH[i])) {
                    System.out.println(tNameCH[i]);
                    numFlg = true;
                    continue;
                } else {
                    if (tNameCH[i] == '-' || !numFlg) {
                        break;
                    }
                    System.out.println(tNameCH[i]);
                    String newFolderName = name.substring(0, i + 1) + "-" + name.substring(i + 1, name.length());
                    System.out.println(newFolderName);
                    File newFolder = new File(folder.getParent() + File.separator + newFolderName.toUpperCase());
                    folder.renameTo(newFolder);
                    break;
                }
            }
        }


    }
}

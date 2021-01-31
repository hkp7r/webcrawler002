package com.folder;

import java.io.File;

public class EditFolderName2UpperCase {
    public static void main(String[] args) {
        String rootFolderPath = "\\\\192.168.31.177\\bt下载\\待处理";
        File rootFolder = new File(rootFolderPath);

        for (File folder : rootFolder.listFiles()) {
            if (folder.isDirectory()) {
                try {
                    folder.renameTo(new File(folder.getParent() + File.separator + folder.getName().toUpperCase()));
                } catch (Exception e) {
                    System.out.println(e.toString());
                } finally {
                    continue;
                }

            }
        }
    }
}

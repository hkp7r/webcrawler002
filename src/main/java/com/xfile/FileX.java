package com.xfile;

import java.io.File;

public class FileX {

    public static void main(String[] args) {

        //File f = new File("\\\\synology\\OWDownload\\CESD944\\uun59.mp4");
        File rootfolder = new File("\\\\synology\\BT下载\\待处理");
        System.out.println(rootfolder.length());

//        String path = "\\\\synology\\OWDownload\\CESD944\\";
//        File f1 = new File(path);
//        if (f1.isDirectory()) {
//            for (File f2 : f1.listFiles()) {
//                System.out.println(f2.getName() + ":" + f2.length());
////                System.out.println(f2.length());
//
//                if (f2.length() < 200000000) {
//                    f2.delete();
//                }
//            }
//        }

        for (File subFile1 : rootfolder.listFiles()) {
            System.out.println(subFile1.getName());

            if (subFile1.isDirectory()) {
                for (File subFile2 : subFile1.listFiles()) {
                    if (subFile2.isDirectory()) {
                        if (subFile2.getName().contains("文") ||
                                subFile2.getName().contains("2020最強") ||
                                subFile2.getName().contains("APP") ||
                                subFile2.getName().contains("本日") ||
                                subFile2.getName().contains("這") ||
                                subFile2.getName().contains("最新") ||
                                subFile2.getName().contains("1024") ||
                                subFile2.getName().contains("XXXXXX")
                        ) {
                            delDir(subFile2);
                        }
                    }
                    if (subFile2.isFile()) {

                        String[] names = subFile2.getName().split("\\.");
                        if (names[names.length - 1].equals("gif") ||
                                names[names.length - 1].equals("torrent") ||
                                names[names.length - 1].equals("png") ||
                                names[names.length - 1].equals("apk") ||
                                names[names.length - 1].equals("txt") ||
                                names[names.length - 1].equals("chm") ||
                                names[names.length - 1].equals("url") ||
                                names[names.length - 1].equals("mht")) {
                            subFile2.delete();
                        }
                        if (names[names.length - 1].equals("mp4")) {
                            if (subFile2.length() < 200000000) {
                                subFile2.delete();
                            }
                        }
                        if (names[names.length - 1].equals("wmv")) {
                            if (subFile2.length() < 200000000) {
                                subFile2.delete();
                            }
                        }
                        if (names[names.length - 1].equals("avi")) {
                            if (subFile2.length() < 200000000) {
                                subFile2.delete();
                            }
                        }
                        if (names[names.length - 1].equals("jpg")) {
                            if (names[0].equals("123") ||
                                    names[0].equals("321") ||
                                    subFile2.getName().contains("草榴") ||
                                    subFile2.getName().contains(".COM") ||
                                    subFile2.getName().contains(".net") ||
                                    subFile2.getName().contains("台湾") ||
                                    subFile2.getName().contains("美女") ||
                                    subFile2.getName().contains("最新") ||
                                    subFile2.getName().contains("地址") ||
                                    names[0].contains("xxxxxxx") ||
                                    names[0].contains("xxxxxxx")
                            ) {
                                subFile2.delete();
                            }
                        }

                        if (subFile2.getName().equals(".DS_Store")) {
                            subFile2.delete();
                        }
                    }
                }
            }
        }
    }

    public static void delDir(File dir) {
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else {
                delDir(f);
                f.delete();
            }
        }
        dir.delete();
    }
}

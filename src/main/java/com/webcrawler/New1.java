package com.webcrawler;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

public class New1 {
    public static void main(String[] args) {
        String url = "http://k6.7086xx.org/pw/html_data/3/2101/5155791.html";
        String optFol = "D:\\BT1024";

//        String optFol = "\\\\192.168.31.177\\bt下载\\bt_torrent";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element eleByid = doc.getElementById("read_tpc");
        Elements eleByClass = doc.getElementsByClass("f14");
        Element eleFolName = doc.getElementById("subject_tpc");
        Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");
        String folName = FilePattern.matcher(eleFolName.html()).replaceAll("");
        String[] strArr = eleByid.html().split("\n");

        File magnetFile = FileUtils.getFile(optFol + "\\" + folName + "\\" + "magnetFile.txt");
        if (magnetFile.exists()) {
            magnetFile.delete();
        }
        //FileOutputStream fos = new FileOutputStream(magnetFile);
        //OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        //BufferedWriter bw = new BufferedWriter(osw);

        int imgId = 1;
        int imgIdx = 1;
        for (String str : strArr) {
            String str1 = String.format("%04d", imgId);
            String str2 = String.format("%02d", imgIdx);
            String imgUrl = Jsoup.parse(str).select("img[src]").attr("src").trim();
            if (!imgUrl.isEmpty()) {

                //System.out.println(Jsoup.parse(str).select("img[src]").attr("src") + " " + str1 + "_" + str2);
                String[] str3 = imgUrl.split("\\.");

                String fileName = optFol + "\\" + folName + "\\" + str1 + "_" + str2 + "." + str3[str3.length - 1];
                System.out.println(fileName);
                if (fileName.contains("gif")) {
                    System.out.println(fileName);
                }
                gitSource(imgUrl, fileName, null);
                imgIdx++;
            }
            String torrentUrl = Jsoup.parse(str).select("a[href]").attr("href").trim();
            if (!torrentUrl.isEmpty() && torrentUrl.contains("torrent")) {
                //&& torrentUrl.startsWith("https://")
                //System.out.println(Jsoup.parse(str).select("a[href]").attr("href"));

                Document hrefDoc = null;
                try {
                    hrefDoc = Jsoup.connect(torrentUrl).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements torrentElements = hrefDoc.select("a[href]");
                for (Element torrentEle : torrentElements) {
                    String torrentHref = torrentEle.attr("href");
                    String torrentText = torrentEle.text();
                    if (torrentText.equals("磁力連結")) {
                        //System.out.println("磁力連結:" + torrentHref);
                        try {
                            FileUtils.write(magnetFile, torrentHref + "\r\n", "UTF-8", true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (torrentText.equals("下載檔案")) {
                        //System.out.println("下載檔案:" + torrentHref);
                        String[] http = torrentUrl.split("/");
                        String httpUrl = http[0] + "//" + http[2];
                        String downloadUrl = httpUrl + torrentHref;

                        String fileName = optFol + "\\" + folName + "\\" + str1 + "_" + str2 + ".torrent";
                        System.out.println(fileName);
                        gitSource(downloadUrl, fileName, torrentUrl);
                        imgId++;
                        imgIdx = 1;
                    }
                }
            }
        }
        //bw.close();
        //osw.close();
        //fos.close();
        System.out.println("処理完了");
    }

    private static void gitSource(String sourceUrl, String fileName, String referer) {
        URLConnection urlcon = null;
        try {
            urlcon = new URL(sourceUrl).openConnection();

            urlcon.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; MyIE2; .NET CLR 1.1.4322)");
            urlcon.setRequestProperty("Referer", referer);
            if (!FileUtils.getFile(fileName).exists()) {
                FileUtils.copyToFile(urlcon.getInputStream(), new File(fileName));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
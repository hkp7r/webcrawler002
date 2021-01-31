package com.webcrawler;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class WebAnalyze_1PageMulitFilm {
    public static void main(String[] args) throws IOException {

        //解析対象URL
        String onePageMulitFilmUrl = "https://k6.colin1994.net/pw/html_data/3/2101/5145133.html";

        //格納フォルダ
        String optFol = "D:\\BT1024\\";

        Document doc = Jsoup.connect(onePageMulitFilmUrl).get();
        Elements imgElements = doc.select("img[src]");
        Elements hrefElements = doc.select("a[href]");

        URL imgUrl = null;
        for (Element ele : imgElements) {
            String imgSrc = ele.attr("src");
            if (!imgSrc.endsWith(".jpg") && !imgSrc.endsWith(".jpeg")) {
                continue;
            }
            System.out.println("画像URL：" + imgSrc);

            String[] urlArr = imgSrc.split("/");
            //System.out.println(urlArr[urlArr.length - 1]);
            String ImgName = urlArr[urlArr.length - 1];

            URLConnection urlcon = new URL(imgSrc).openConnection();
            urlcon.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; MyIE2; .NET CLR 1.1.4322)");

            File optFile = new File(optFol + ImgName);

            if (!optFile.exists()) {
                FileUtils.copyToFile(urlcon.getInputStream(), optFile);
            }
        }


        File magnetFile = new File(optFol + "magnetFile.txt");
        FileOutputStream fos = new FileOutputStream(magnetFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);

        int idx = 1;
        for (Element ele : hrefElements) {
            String href = ele.attr("href");


            if (href.startsWith("https://") && href.contains("torrent")) {
                System.out.println("ダウンロード：" + href);

                Document hrefDoc = Jsoup.connect(href).get();
                Elements torrentElements = hrefDoc.select("a[href]");

                for (Element torrentEle : torrentElements) {

                    String torrentHref = torrentEle.attr("href");
                    String torrentText = torrentEle.text();
                    if (torrentText.equals("磁力連結")) {
                        System.out.println("磁力連結:" + torrentHref);
                        bw.write(torrentHref + "\r\n");
                    }
                    if (torrentText.equals("下載檔案")) {
                        System.out.println("下載檔案:" + torrentHref);
                        String[] http = href.split("/");
                        String httpUrl = http[0] + "//" + http[2];
                        String downloadUrl = httpUrl + torrentHref;

                        URL torrentUrl = new URL(downloadUrl);
                        URLConnection urlcon = torrentUrl.openConnection();
                        urlcon.setRequestProperty("User-agent", "User-agent");
                        urlcon.setRequestProperty("Referer", href);

                        File optFile = new File(optFol + String.format("%04d", idx) + ".torrent");

                        if (!optFile.exists()) {
                            FileUtils.copyToFile(urlcon.getInputStream(), optFile);
                        }

                        idx++;
                    }
                }
            }
        }
        bw.close();
        osw.close();
        fos.close();
    }
}

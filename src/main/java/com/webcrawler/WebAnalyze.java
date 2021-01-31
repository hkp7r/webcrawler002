package com.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

public class WebAnalyze {
    public static void main(String[] args) throws IOException {

        File imgFile = new File("C:\\Download\\BT1024\\亞洲無碼\\img.txt");
        FileOutputStream fos = new FileOutputStream(imgFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);


        //URL
        String outputFolder = "C:\\Download\\BT1024\\亞洲無碼\\";
        String baseUrl = "https://nm.nmcsym.com/pw/";
        String mainUrl = "https://nm.nmcsym.com/pw/";
        String fidTaget = "5";

        int videoId = 1;

        //https://nm.nmcsym.com/pw/thread.php?fid=5
        //https://nm.nmcsym.com/pw/thread.php?fid=80

        Document doc = Jsoup.connect(mainUrl).get();
        Elements links = doc.select("a[href]");

        String linkHrefTaget = "";
        for (Element link : links) {
            String linkHref = link.attr("href");
            if (linkHref.indexOf("php?fid=") < 0) {
                continue;
            } else {
                System.out.println(linkHref + " " + link.text());

            }
            String[] fidArray = linkHref.split("=");
            String fid = fidArray[fidArray.length - 1];

            if (fidTaget.equals(fid)) {
                linkHrefTaget = linkHref;
            }

        }

        int pageIdx = 1;
        while (pageIdx <= 5) {
            String docSub1Url = baseUrl + linkHrefTaget + "&page=" + pageIdx;
            System.out.println("解析対象URL：" + docSub1Url);
            System.out.println("==============================================");

            Document docSub1 = Jsoup.connect(docSub1Url).get();
            Elements docSub1Links = docSub1.select("a[href]");
            String subStr1 = "html_data/" + fidTaget;
            String fileName = "";
            for (Element link : docSub1Links) {
                String linkHref = link.attr("href");

                if (linkHref.indexOf(subStr1) < 0 || link.text().equals(".::")) {
                    continue;
                } else {
                    fileName = String.format("%04d_", videoId++) + link.text();
                    System.out.println("映画明細：" + linkHref + "　　　　　" + fileName);

                    getImg(baseUrl + linkHref, fileName, outputFolder,bw);

                    bw.flush();
                    getTorrent(baseUrl + linkHref, fileName, outputFolder);
                }
            }
            pageIdx++;
        }
    }


    public static void getImg(String strUrl, String fileName, String outputFolder,BufferedWriter bw) {

        try {
            Document doc = Jsoup.connect(strUrl).get();
            Elements links = doc.select("img[src]");

            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bosImg = null;

            URL imgUrl = null;

            int len = 0;
            int imgId = 1;

            Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

            for (Element link : links) {
                String linkHref = link.attr("src");
                String linkText = link.attr("alt");
                if (!linkHref.startsWith("http")) {
                    continue;
                }
                System.out.println(linkHref + " " + linkText);

                String[] strArr = linkHref.split("\\.");

                // ★★★★★
                bw.write(linkHref + "   " + FilePattern.matcher(fileName).replaceAll("") + "_" + String.format("%02d", imgId++) + "." + strArr[strArr.length - 1]+"\r\n");

                /*
                imgUrl = new URL(linkHref);
                URLConnection urlcon = imgUrl.openConnection();
                urlcon.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; MyIE2; .NET CLR 1.1.4322)");
                is = urlcon.getInputStream();
                bis = new BufferedInputStream(is);

                byte[] data = new byte[1024];
                bosImg = new BufferedOutputStream(new FileOutputStream(new File(outputFolder + FilePattern.matcher(fileName).replaceAll("") + "_" + String.format("%02d", imgId++) + "." + strArr[strArr.length - 1])));
                while ((len = bis.read(data)) != -1) {
                    bosImg.write(data, 0, len);
                }
                bosImg.close();
                bis.close();
                is.close();
*/

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getTorrent(String url, String fileName, String outputFolder) {
        try {

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

            for (Element link : links) {
                String linkHref = link.attr("href");
                if (linkHref.indexOf("https") < 0 || linkHref.indexOf("torrent") < 0) {
                    continue;
                } else {
                    System.out.println("　　Torrentダウンロード画面URL:" + linkHref);
                    Document docTorrent = Jsoup.connect(linkHref).get();
                    Elements linksTorrent = docTorrent.select("a[href]");
                    String[] http = linkHref.split("/");
                    String httpUrl = http[0] + "//" + http[2];

                    for (Element linkTorrent : linksTorrent) {
                        String linkHrefTorrent = linkTorrent.attr("href");
                        if (linkHrefTorrent.indexOf("Download") < 0) {
                            continue;
                        } else {
                            System.out.println("　　　TorrentファイルURL：" + httpUrl + linkHrefTorrent);


                            URL urlTorrent = new URL(httpUrl + linkHrefTorrent);
                            URLConnection con = urlTorrent.openConnection();

                            con.setRequestProperty("User-agent", "User-agent");
                            con.setRequestProperty("Referer", linkHref);
                            //String[] content = con.getHeaderField("Content-Disposition").split("=");

                            InputStream isTorrent = con.getInputStream();
                            BufferedInputStream bisTorrent = new BufferedInputStream(isTorrent);
                            BufferedOutputStream bosTorrent = new BufferedOutputStream(new FileOutputStream(new File(outputFolder + FilePattern.matcher(fileName).replaceAll("") + ".torrent")));

                            int len = 0;
                            byte[] data = new byte[1024];
                            while ((len = bisTorrent.read(data)) != -1) {
                                bosTorrent.write(data, 0, len);
                            }

                            bosTorrent.close();
                            bisTorrent.close();
                            isTorrent.close();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;

public class soupHtml {
    private static String[] file_route = {
          "/Users/jameslee/konkuk_git/week02/SimpleIR/떡.html",
          "/Users/jameslee/konkuk_git/week02/SimpleIR/라면.html",
          "/Users/jameslee/konkuk_git/week02/SimpleIR/아이스크림.html",
          "/Users/jameslee/konkuk_git/week02/SimpleIR/초밥.html",
          "/Users/jameslee/konkuk_git/week02/SimpleIR/파스타.html"
    };
    private static String[] getTitle = new String[5];
    private static String[] getP = new String[5];
    public static void getHTMLParse() throws IOException, ParserConfigurationException, TransformerConfigurationException {
        for(int i=0; i<5; i++) {
            File input = new File(file_route[i]);
            Document doc = Jsoup.parse(input, "UTF-8");

            Elements contents;

            getTitle[i] = doc.select("title").text();
            getP[i] = doc.select("p").append("\n").toString().replaceAll("\\<.*?>", "");
//            System.out.println(getTitle[i]);
//            System.out.println(getP[i]);
        }
        bulidDoc build_food = new bulidDoc(getTitle, getP);
        build_food.xmlDoc();
        build_food.writeXml();
    }
}

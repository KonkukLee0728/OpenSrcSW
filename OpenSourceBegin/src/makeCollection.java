import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
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

class bulidDoc {
    private String[] getTitle;
    private String[] getP;

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    org.w3c.dom.Document food_xml = docBuilder.newDocument();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(food_xml);
    StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/jameslee/konkuk_git/week02/SimpleIR/collection.xml")));

    public bulidDoc(String[] getTitle, String[] getP) throws ParserConfigurationException, IOException, TransformerConfigurationException {
        this.getTitle = getTitle;
        this.getP = getP;
    }

    public void xmlDoc() {
        try {
            Element docs = food_xml.createElement("docs");
            food_xml.appendChild(docs);
            for(int i=0; i<5; i++) {
                Element doc = food_xml.createElement("doc");
                doc.setAttribute("id", Integer.toString(i));
                docs.appendChild(doc);

                Element title = food_xml.createElement("title");
                title.appendChild(food_xml.createTextNode(getTitle[i]));
                doc.appendChild(title);

                Element body = food_xml.createElement("body");
                body.appendChild(food_xml.createTextNode(getP[i]));
                doc.appendChild(body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeXml() {
        try {
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

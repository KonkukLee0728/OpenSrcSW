import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class makeKeyword {
    String[] getTitle = new String[5];
    String[] getP = new String[5];

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    org.w3c.dom.Document food_xml = docBuilder.newDocument();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(food_xml);
    StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/jameslee/konkuk_git/week02/SimpleIR/index.xml")));

    public makeKeyword() throws TransformerConfigurationException, FileNotFoundException, ParserConfigurationException {
    }

//    public makeKeyword(String[] getTitle, String[] getP) throws ParserConfigurationException, IOException, TransformerConfigurationException {
//        this.getTitle = getTitle;
//        this.getP = getP;
//    }

    public void readXml(String xmlFileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFileName);

        NodeList fileList;
        fileList = doc.getElementsByTagName("doc");

        for(int i=0; i<fileList.getLength(); i++) {
            for(Node node = fileList.item(i).getFirstChild(); node!=null; node=node.getNextSibling()) {
                if(node.getNodeName().equals("title")) {
                    getTitle[i] = node.getTextContent();
                } else if(node.getNodeName().equals("body")) {
                    getP[i] = node.getTextContent();
                }
            }

        }
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
                KeywordList kl = extractKka(getP[i]);
                for(int j=0; j<kl.size(); j++) {
                    Keyword kwrds = kl.get(j);

                    body.appendChild(food_xml.createTextNode(kwrds.getString() + ":" + kwrds.getCnt() + "#"));
                }


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

    public KeywordList extractKka(String getP) {
        String extractString = getP;

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(extractString, true);
        return kl;
    }
}

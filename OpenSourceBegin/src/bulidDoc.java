import org.w3c.dom.Document;
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
import java.io.*;

public class bulidDoc {
    private String[] getTitle;
    private String[] getP;

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    Document food_xml = docBuilder.newDocument();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(food_xml);
    StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/jameslee/konkuk_git/week02/SimpleIR/doc.xml")));

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

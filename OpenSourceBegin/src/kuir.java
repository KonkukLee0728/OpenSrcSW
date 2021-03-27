import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;

public class kuir {
    public static void main(String[] args) throws Exception {

        //html parsing
        makeCollection start = new makeCollection(args[1]);
        start.getHTMLParse();

        //변수
        String[] getTitle = makeCollection.getGetTitle();
        String[] getP = makeCollection.getGetP();
        if(args[0].equals("-c")) {
            System.out.println("collection.xml complete");
            //collection.xml만들기
            bulidDoc build_food = new bulidDoc(getTitle, getP);
            build_food.xmlDoc();
            build_food.writeXml();
        } else if(args[0].equals("-k")) {
            //index.xml 만들기
            System.out.println("index.xml complete");
            makeKeyword mk = new makeKeyword(getTitle, getP);
            mk.xmlDoc();
            mk.writeXml();
        } else if(args[0].equals("-i")) {
            indexer test = new indexer();
            indexer.readXml(new File(args[1]+"index.xml"));
//            indexer.readXml(new File("/Users/jameslee/konkuk_git/week02/SimpleIR/index.xml"));
        }
    }
}

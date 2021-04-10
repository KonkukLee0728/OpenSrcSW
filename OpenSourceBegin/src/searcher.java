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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class searcher {
    int MAX = 50;
    int LEN = 0;
    String[] word = new String[MAX];
    int[] count = new int[MAX];
    String[] getTitle = new String[5];
    String[] getBody = new String[5];
    Double querySum= 0.0;

    public void queryInput(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Query 입력: ");
        String userInput = scan.nextLine();

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(userInput, true);

        for(int i=0; i<kl.size(); i++) {
            Keyword kwrd = kl.get(i);
            word[i]=kwrd.getString();
            count[i]=kwrd.getCnt();
            querySum+=kwrd.getCnt();
            LEN++;
        }
        querySum=Math.sqrt(querySum);
    }

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
                    getBody[i] = node.getTextContent();
                }
            }

        }
    }

    public void readHash(String fileLoc) throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream(fileLoc);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;

        CalcSim(hashMap);
    }

    public void CalcSim(HashMap hashMap) {
        Iterator<String> it = hashMap.keySet().iterator();
        Double[] SimResult = new Double[10];
        Double[] SimResultSum = new Double[10];
        int[] DocID = new int[10];
        for(int i=0; i<SimResult.length; i++) {
            SimResult[i] = 0.0;
            SimResultSum[i] = 0.0;
            DocID[i]=i;
        }

        while(it.hasNext()) {
            String key = it.next();

            if(key ==  null)
                continue;
            for(int i=0; i<LEN; i++) {
                if (key.equals(word[i])){
                    String getIndex = (String) hashMap.get(key);
                    String[] tmt = new String[10];
                    tmt=getIndex.split(" ");

                    for(int j=0; j<tmt.length; j+=2) {
                        int doc_id = Integer.parseInt(tmt[j]);
                        double value = Double.parseDouble(tmt[j+1]);
                        SimResult[doc_id] += value * count[i];
                        SimResultSum[doc_id] += value;
                    }
                }
            }
        }

        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                if(SimResult[i]>SimResult[j]) {
                    Double tmp = SimResult[i];
                    SimResult[i] = SimResult[j];
                    SimResult[j] = tmp;

                    tmp = SimResultSum[i];
                    SimResultSum[i] = SimResultSum[j];
                    SimResultSum[j] = tmp;

                    int tmt = DocID[i];
                    DocID[i]=DocID[j];
                    DocID[j] = tmt;
                }
                else if(DocID[i]<DocID[j]) {
                    Double tmp = SimResult[i];
                    SimResult[i] = SimResult[j];
                    SimResult[j] = tmp;

                    tmp = SimResultSum[i];
                    SimResultSum[i] = SimResultSum[j];
                    SimResultSum[j] = tmp;

                    int tmt = DocID[i];
                    DocID[i]=DocID[j];
                    DocID[j] = tmt;
                }
            }
        }

        for(int i=0; i<3; i++) {
            System.out.println((i+1)+". "+ getTitle[DocID[i]] + " " +SimResult[i]);
        }

        for(int i=0; i<3; i++) {
            Double resultcos;
            resultcos = SimResult[i] / (querySum + Math.sqrt(SimResult[i]));
            System.out.println(resultcos);
        }
    }
}

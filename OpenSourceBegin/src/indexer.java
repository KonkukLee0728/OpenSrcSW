import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;


public class indexer {
    static HashMap<String, Integer> vocCount = new HashMap<>();
    static int DOCCNT = 5;
    static int MAX = 2000;
    static int index = 0;
    static String[] docVoc = new String[MAX];
    static int[][] inDoc = new int[MAX][DOCCNT*2];
    static float[][] calResult = new float[MAX][DOCCNT*2];
    static HashMap<String, String> saveResult = new HashMap<>();

    public static void readXml(File xmlFile) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        NodeList docsTagList = document.getElementsByTagName("docs");

        inDoc = initializeArray(inDoc);

        for(int i=0; i<docsTagList.getLength(); i++) {
            Node fileNode = docsTagList.item(i);
            Element fileElement = (Element) fileNode;

            NodeList docList = fileElement.getElementsByTagName("doc");
            for(int j=0; j<docList.getLength(); j++) {
                Node docNode = docList.item(j);
                Element docElement = (Element) docNode;

                Node y = ((Element) docNode).getElementsByTagName("title").item(0);
                docNode.removeChild(y);

                String docId = docElement.getAttribute("id");
                String test = docNode.getTextContent();

                test.trim();
                test.replaceAll("\n    ", "");
                String[] arr = test.split("#");

                //todo calIndexer
                calIndexer(docId, arr);

            }
        }
        calculateValue();
        createHash();
        saveHash();
        readHash();
    }

    public static void calIndexer(String docId, String[] input) {

        for (String com : input) {
            if (com == input[input.length - 1])
                break;
            if (input[0] == com) {
                String tmp = com.substring(18);
                com = tmp;

            }
            String[] tmt = com.split(":");

            //todo calculate

            if(vocCount.containsKey(tmt[0])) {
                int cnt = vocCount.get(tmt[0]);
                cnt++;
                vocCount.put(tmt[0], cnt);
            } else {
                vocCount.put(tmt[0], 1);
            }

//            docVoc[index] = tmt[0];

            int existVocIndex=0; //존재 위
            int flag=0; //flag 1이면 이미 존재하는값 0이면 새로운

            for(int i=0; i<=index; i++) {
                if(tmt[0].equals(docVoc[i])) {
                    existVocIndex=i;
                    flag=1;
                    break;
                }
            }
            if(flag == 0) {
                docVoc[index] = tmt[0];
            }

            if(flag == 1) {
                for(int i=0; i<DOCCNT*2; i++) {
                    if(inDoc[existVocIndex][i]==-1) {
                        int tnt = Integer.parseInt(docId);
                        inDoc[existVocIndex][i] = tnt;
                        tnt = Integer.parseInt(tmt[1]);
                        inDoc[existVocIndex][i+1] = tnt;
                        flag=0;
                        break;
                    }
                }
            } else {
                for(int i=0; i<DOCCNT*2; i++) {
                    if(inDoc[index][i]==-1) {
                        int tnt = Integer.parseInt(docId);
                        inDoc[index][i] = tnt;
                        tnt = Integer.parseInt(tmt[1]);
                        inDoc[index][i+1] = tnt;
                        index++;
                        break;
                    }
                }
            }

        }

        //가중치 calculate
//        calculateValue(docVoc, inDoc, input.length);
    }

    public static void calculateValue() {

        float wResult;

        for(int i=0; i<=index; i++) {
            for(int j=0; j<DOCCNT*2; j+=2) {
                if(inDoc[i][j]!=-1) {
                    //가중치 계산 공식
                    wResult = (float) (inDoc[i][j+1] * Math.log(DOCCNT / vocCount.get(docVoc[i])));
                    calResult[i][j] = inDoc[i][j];
                    calResult[i][j+1] = wResult;
                } else
                    break;;
            }
        }
    }

    public static void createHash() {
        for(int i=0; i<=index; i++) {
            String form = "";
            for(int j=0; j<DOCCNT; j+=2) {
                if(inDoc[i][j]!=-1) {
                    int a = (int) calResult[i][j];
                    form+= String.valueOf(a) + " ";
                    form+= String.valueOf(calResult[i][j+1]) +" ";
                } else
                    break;
            }
            saveResult.put(docVoc[i], form);
//            saveResult.put(docVoc[i], calResult[i]);
        }
    }

    public static void saveHash() throws IOException {
        //파일에 객체 저장
        FileOutputStream fileStream = new FileOutputStream("/Users/jameslee/konkuk_git/week02/SimpleIR/index.post");

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

        objectOutputStream.writeObject(saveResult);

        objectOutputStream.close();
    }

    public static void readHash() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("/Users/jameslee/konkuk_git/week02/SimpleIR/index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;
        System.out.println("가중치를 찾고싶은 키워드를 입력하시오");
        Scanner scan = new Scanner(System.in);
        String inputKey = scan.next();
        System.out.println(hashMap.get(inputKey));
    }

    public static int[][] initializeArray(int[][] inDoc) {
        for(int i=0; i<inDoc.length; i++)
            for(int j=0; j<inDoc[i].length; j++)
                inDoc[i][j]=-1;

        return inDoc;
    }
}

import java.io.File;

public class kuir {
    public static void main(String[] args) throws Exception {

        if(args[0].equals("-c")) {
            makeCollection start = new makeCollection(args[1]);
            makeCollection.getHTMLParse();
            String[] getTitle = makeCollection.getGetTitle();
            String[] getP = makeCollection.getGetP();
            System.out.println("collection.xml complete");
            //collection.xml만들기
            bulidDoc build_food = new bulidDoc(getTitle, getP);
            build_food.xmlDoc();
            build_food.writeXml();
        } else if(args[0].equals("-k")) {
            //index.xml 만들기
            System.out.println("index.xml complete");
            makeKeyword mk = new makeKeyword();
            mk.readXml(args[1]);
            mk.xmlDoc();
            mk.writeXml();
        } else if(args[0].equals("-i")) {
            indexer test = new indexer();
            indexer.readXml(new File(args[1]));
//            indexer.readXml(new File("/Users/jameslee/konkuk_git/week02/SimpleIR/index.xml"));
        } else if(args[0].equals("-s")) {
            if(args[2].equals("query")) {
                searcher a = new searcher();
                a.readXml("/Users/jameslee/konkuk_git/week02/SimpleIR/collection.xml");
                a.queryInput();
                a.readHash("/Users/jameslee/konkuk_git/week02/SimpleIR/index.post");
            }
        }

    }
}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class genSnippet {
    String[] array = new String[10];
    int[] similar = new int[10];
    int lineNum = 0;
    String[] compareQ = new String[10];

    public void main(String[] args) throws FileNotFoundException {
        input(args[1]);
        String query = args[3];

        compareQ = query.split(" ");

        processing();
    }

    public void input(String input) throws FileNotFoundException {//input.txt입력받기
        FileInputStream IN = new FileInputStream(input);
        Scanner scan = new Scanner(IN);
        int i=0;
        while(scan.hasNext()) {
            array[i] = scan.next();
            lineNum++;
        }
    }

    public void processing() {
        for(int i=0; i<lineNum; i++) {

            for(int j=0; j<10; j++) {

                if(array[i].contains(compareQ[j])) { //키워드 포함시 유사도 증가
                    similar[i]++;
                }
            }

        }

        int resultIndex = 0;

        for(int i=0; i<lineNum; i++) {
            for(int j=0; j<lineNum; j++) {
                if(similar[i] > similar[j]) {
                    resultIndex = i;
                }
            }
        }

        System.out.println(array[resultIndex]);
    }
}

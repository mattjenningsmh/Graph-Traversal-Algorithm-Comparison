import java.util.*;

class Tester {
    public static void main(String[] args) {
        List<Character> listOfChars = new ArrayList<>();
        int range = 122 - 97;
        char a;
        //for (int i = 0; i <= range; i++) {
        //    a = (char) ('a' + i);
        //    listOfChars.add(a);
        //}
        List<String> listOfStrings = new ArrayList<>(); 
        int nodeInt = 1;
        String maxNode = "";
        for(int i = 0; i<=100; i++){
            maxNode = "" + (char)('a'+i%26) + nodeInt;
            listOfStrings.add(maxNode); 
            if(i%26 == 0){
                nodeInt++; 
            }
        }


        GraphGenerator<String> strGraph = new GraphGenerator<>(listOfStrings); 
        strGraph.createGridGraph(10);
        List<List<String>> arr2D = strGraph.get2DArray();
        strGraph.printArray2D();
        System.out.println(strGraph.printGraph(10));
        //System.out.println(strGraph.printGraph(100));
        //System.out.println(strGraph);
        //GraphGenerator<Character> graph = new GraphGenerator<>(listOfChars);
        //graph.createRandomGraph(26);
        //System.out.println(graph);

        //graph.clearEdges();

        //graph.createGridGraph(5);
        //System.out.println(graph);
        //System.out.println(graph.printGraph(5));
        System.out.printf("maximum node = %s\n",maxNode);
    }
}

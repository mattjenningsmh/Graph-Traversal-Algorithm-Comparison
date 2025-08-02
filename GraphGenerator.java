import java.util.*;


/**
 * GraphGenerator
 */
public class GraphGenerator<K> {
    // max edge weight
    private int max_edge_weight = 100;

    // Underlying data structures
    private Map<K, Map<K, Integer>> map;
    private List<K> nodes;
    // Random
    private int seed = 3;
    private Random rand;
    
    //2D array representing grid node positions  
    private List<List<K>> g;

    public GraphGenerator(Collection<K> nodes) {
        this.map = new HashMap<>();
        this.rand = new Random(seed);
        for (K key : nodes) {
            map.put(key, new HashMap<>());
        }
        this.nodes = new ArrayList<>(map.keySet());
    }

    public void clearEdges() {
        for (K node : nodes) {
            map.get(node).clear();
        }
    }
    public void setMaxEdgeWeight(int n){
        this.max_edge_weight = n; 
    }
    // returns the underlying map. remove this in the future and implement map
    // methods manually.
    public Map<K, Map<K, Integer>> getGraph() {
        return map;
    }
    
    /*
     * generate grid graph with random weight
     * 
     * @param size - this is a size x size graph
     */
    public void createGridGraph(int size, int maxWeight) {
        // Generate 2d array
        g = toArray(size); 
        // Generate undirected weighted edges in grid        
        gridEdges(g,maxWeight); 
    }

    private void gridEdges(List<List<K>> g, int maxWeight){
        int maxSize = g.size(); 
        for (int i = 0; i < g.size(); i++) {
            for (int j = 0; j < g.get(i).size(); j++) {
                K curr = g.get(i).get(j);
                if(j+1 < maxSize){
                    K right = g.get(i).get(j+1);
                    addUndirectedEdge(curr, right, maxWeight);
                }
                if(i+1 < maxSize){
                    K down = g.get(i+1).get(j); 
                    addUndirectedEdge(curr, down, maxWeight);
                }
            }
        }
    }

    public List<List<K>> get2DArray(){
        return g;
    }
    private List<List<K>> toArray(int size) {
        if(size*size > nodes.size()){
            throw new IllegalArgumentException("size^2 must be less or equal to the number of nodes in the graph"); 
        }
        List<List<K>> g = new ArrayList<>();
        List<K> temp = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            temp.add(nodes.get(i));
            if (temp.size() >= size) {
                g.add(new ArrayList<>(temp));
                temp.clear();
            }
        }
        return g;
    }

    // Generate a Graph with with given nodes and nodes.size() + numEdges
    // randomized edges
    public void createRandomGraph(int numEdges) {
        ensureConnectivity();

        int maxEdges = nodes.size() * (nodes.size() - 1);
        int i;
        // you can only have n * (n-1) edges in a directed no self-loop graph.
        for (i = 0; i < numEdges && i < maxEdges - nodes.size(); i++) {
            genRandEdges(numEdges, maxEdges);
        }
        System.out.println((nodes.size() + i) + " edges added");
    }

    public String toString() {
        String strGraph = "";
        for (Map.Entry<K, Map<K, Integer>> entry : map.entrySet()) {
            // System.out.printf("key: %c Value: " + entry.getValue(), entry.getKey());
            K key = entry.getKey();
            Map<K, Integer> val = entry.getValue();
            strGraph += String.format("Key: %s, Value: %s\n", key, val);
        }
        return strGraph;
    }
    public String printGraph(int size){
        List<List<K>> g = toArray(size); 
        String bel = "|  ";
        String res = ""; 
        //[
        //[1,2,3], i=0
        //[4,5,6], i=1
        //[7,8,9]  i=2
        //]
        int rowInd = 0;
        for(List<K> list: g){
            for(int i = 0; i<list.size(); i++){
                res += (i==list.size()-1) ? String.format("%s\n", list.get(i)) : String.format("%s-", list.get(i));
            }
            res+= (rowInd < g.size()-1) ? bel.repeat(size) + "\n" : ""; 
            rowInd++; 
        }
        return res; 
    }
    public void printArray2D(){
        for(List<K> arr : g){
            System.out.println(arr);
        }
    }


    private void addEdge(K curr, K next, int i) {
        map.get(curr).put(next, i);
    }

    // add an undirected edge of a random weight
    private void addUndirectedEdge(K curr, K next, int maxWeight) {
        int edgeWeight = randInt(max_edge_weight)+1;
        if (maxWeight == 1){
            edgeWeight = 1;
        }
        if (!map.get(curr).containsKey(next)) {
            map.get(curr).put(next, edgeWeight);
            map.get(next).put(curr, edgeWeight);
        }
    }

    private int randInt(int range) {
        return rand.nextInt(range);
    }

    private void ensureConnectivity() {
        // System.out.println("ensureConnectivity called");
        int rNum;
        for (int i = 0; i < nodes.size(); i++) {
            rNum = rand.nextInt(max_edge_weight);
            K curr = nodes.get(i);
            K next = nodes.get((i + 1) % nodes.size());
            addEdge(curr, next, rNum);
        }
    }

    // Helper Method for generating Random graph
    private void genRandEdges(int numEdges, int maxEdges) {
        List<K> possNext;

        K curr;
        Set<K> triedNodes = new HashSet<>(nodes);
        do {
            possNext = new ArrayList<>(nodes);

            // pick random node
            curr = nodes.get(randInt(nodes.size()));
            triedNodes.remove(curr);
            possNext.remove(curr);

            // remove current paths from possible paths
            possNext.removeAll(map.get(curr).keySet());
        } while (possNext.size() < 1 && triedNodes.size() > 0);
        K next = possNext.get(randInt(possNext.size()));
        addEdge(curr, next, randInt(100));
    }
}

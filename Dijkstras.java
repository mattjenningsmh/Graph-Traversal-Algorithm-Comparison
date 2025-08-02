import java.util.*;

public class Dijkstras<K> {
    List<List<K>> arr2D;
    Map<K, Map<K, Integer>> weights;

    class Node implements Comparable<Node> {
        int[] node;
        Integer distance;

        Node(int[] node, Integer distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            if (this.distance < other.distance)
                return -1;
            if (this.distance > other.distance)
                return 1;
            else
                return 0;
        }
        @Override 
        public String toString(){
            return "Position: "+this.node[0] + ", " + this.node[1] + " | Distance " + this.distance;
        }
    }

    public Dijkstras(List<List<K>> arr2D, Map<K, Map<K, Integer>> weights) {
        this.arr2D = arr2D;
        this.weights = weights;
    }

    private int[] getNodePosition(K node) {
        int coords[] = { 0, 0 };
        for (List<K> list : arr2D) {
            for (K elem : list) {
                if (elem.equals(node)) {
                    return coords;
                }
                coords[1] += 1;
            }
            coords[1] = 0;
            coords[0] += 1;
        }
        return coords;
    }
    private static void print(Object input){
        System.out.println(input);
    }


    
    // run dijkstras algorithm
    // While the function take in nodes of type K, I will be using all nodes
    // relative positions
    // to speed up computation just like in the other algorithms
    public Integer run(K start, K end) {
        Queue<Node> pq = new PriorityQueue<>();
        Map<List<Integer>, Integer> visited = new HashMap<>();
        int[] startPos = getNodePosition(start);
        int[] endPos = getNodePosition(end);
        for (int i = 0; i < arr2D.size(); i++) {
            for (int j = 0; j < arr2D.get(i).size(); j++) {
                int[] pos = { i, j };
                if (Arrays.equals(startPos, pos)) {
                    pq.offer(new Node(pos, 0));
                    visited.put(List.of(pos[0],pos[1]), 0);
                } else {
                    pq.offer(new Node(pos, Integer.MAX_VALUE));
                    visited.put(List.of(pos[0],pos[1]), Integer.MAX_VALUE);
                    //print(pos[0] + ", " + pos[1]);
                }
            }
        }
        //int[] test = {1,1};
        //pq.offer(new Node(test, 3)); 
        //while (!pq.isEmpty()){
        //    print(pq.poll());
        //}

        // if unvisited is empty or contains only unreachable nodes skip to step 6
        // unreachable nodes are not an issue as I have ensured that graphs are
        // connected
        Node current = new Node(startPos, 0);
        while (!pq.isEmpty()) {
            current = pq.poll();
            //print(current); 
            //print(current);
            // skip if this node has already been visited with a shorter distance
            // this will lazily remove all the nodes after Dijkstras has finished calculating
            // this is fairly efficient
            if (visited.get(List.of(current.node[0],current.node[1])) < current.distance) {
                continue;
            }
            // If the end node is reached, we can stop
            //if (Arrays.equals(current.node, endPos)) {
            //    break;
            //}

            List<Node> neighbors = neighbors(current);
            for (Node node : neighbors) {
                int newDist = current.distance + node.distance; 
                if (visited.get(List.of(node.node[0],node.node[1])) > newDist) {
                    visited.replace(List.of(node.node[0],node.node[1]), newDist);
                    pq.offer(new Node(node.node, newDist));
                }
            }
        }
        return visited.get(List.of(endPos[0],endPos[1])); 
    }

    private List<Node> neighbors(Node current) {
        List<Node> neighbors = new ArrayList<>();
        int[][] positions = {
                { -1, 0 }, // up
                { 0, -1 }, // left
                { 1, 0 }, // down
                { 0, 1 } // right
        };
        for (int[] dir : positions) {
            int[] newPos = { current.node[0] + dir[0], current.node[1] + dir[1] };
            if (inBounds(newPos[0], newPos[1])) {
                neighbors.add(new Node(newPos, getWeight(current.node, newPos)));
            }
        }
        return neighbors;
    }

    private Integer getWeight(int[] current, int[] neighbor) {
        int[] currPos = current;
        int[] neighborPos = neighbor;
        K currentVal = arr2D.get(currPos[0]).get(currPos[1]);
        K neighborVal = arr2D.get(neighborPos[0]).get(neighborPos[1]);
        return weights.get(currentVal).get(neighborVal);
    }

    private boolean inBounds(int row, int col) {
        if ((row < arr2D.size() && row >= 0) && (col < arr2D.get(row).size() && col >= 0)) {
            return true;
        }
        return false;
    }
}

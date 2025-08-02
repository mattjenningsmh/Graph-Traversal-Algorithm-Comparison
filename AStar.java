import java.util.*;

/**
 * AStar
 */
public class AStar<K> {
    // I could develop one A* for grid and another for random directed graph
    private List<List<K>> arr2D;
    Map<K, Map<K, Integer>> weights;

    public AStar(List<List<K>> arr2D, Map<K, Map<K, Integer>> weights) {
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

    private ArrayList<String> reconstructPath(Map<String, int[]> closedList, int[] start, int[] end) {
        ArrayList<String> path = new ArrayList<>();
        int[] next = closedList.get(Arrays.toString(end));
        path.add(Arrays.toString(next));
        while (next != null && !Arrays.equals(next, start)) {
            path.add(Arrays.toString(next));
            next = closedList.get(Arrays.toString(next));
        }
        return path;
    }

    public Integer runAStar(K startNode, K endNode, String heuristic) {
        // get start and end coords
        int start[] = getNodePosition(startNode);
        int end[] = getNodePosition(endNode);
        // initialize open and closed list
        List<int[]> openList = new ArrayList<>();
        Map<String, Double> openFScore = new HashMap<>(); // key: node position as string, value: f score
        Map<String, Double> gScores = new HashMap<>();
        Map<String, int[]> closedList = new HashMap<>(); // For path reconstruction
        Set<String> visitedNodes = new HashSet<>(); // To track visited nodes
                                                    //
        // place the start node on the openList with an f of 0
        openList.add(start);
        openFScore.put(Arrays.toString(start), 0.0);
        gScores.put(Arrays.toString(start), 0.0); // g value for start is 0

        while (!openList.isEmpty()) {
            // check if we reached the end
            if (endFound(openList, end)) {
                return gScores.get(Arrays.toString(end)).intValue(); // Return the total distance (g value)
            }
            // find the node with the least f on the open list
            double minF = Double.MAX_VALUE;
            int minInd = -1;
            for (int i = 0; i < openList.size(); i++) {
                String key = Arrays.toString(openList.get(i));
                double fScore = openFScore.getOrDefault(key, Double.MAX_VALUE);
                if (fScore < minF) {
                    minF = fScore;
                    minInd = i;
                }
            }

            // get the current node
            int[] parent = openList.get(minInd);
            String parentKey = Arrays.toString(parent);

            // remove current from open and add to visited
            openList.remove(minInd);
            visitedNodes.add(parentKey);
            List<int[]> successors = getSuccessors(parent);
            for (int[] successor : successors) {
                String successorKey = Arrays.toString(successor);
                double gScore = computeG(successor,parent);
                double fScore = computeF(heuristic, parent, successor, end);

                // if successor is already in visited list, skip it
                if (visitedNodes.contains(successorKey)) {
                    continue;
                }

                // if successor is not in open list or has a lower f score, update it
                if (!openFScore.containsKey(successorKey) || fScore < openFScore.get(successorKey)) {
                    openList.add(successor);
                    openFScore.put(successorKey, fScore);
                    closedList.put(successorKey, parent); // track path
                                                          //
                    gScores.put(successorKey, gScores.get(parentKey) + gScore);                                     //
                                                          //
                }
            }
        }

        return -1; // return empty path if no path found

    }

    private List<int[]> getSuccessors(int[] current) {
        List<int[]> successors = new ArrayList<>();
        int rows = arr2D.size();
        int cols = arr2D.get(0).size();

        // Check all 4 possible directions (up, down, left, right)
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int newRow = current[0] + dir[0];
            int newCol = current[1] + dir[1];
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                successors.add(new int[] { newRow, newCol });
            }
        }
        return successors;
    }

    private boolean endFound(List<int[]> successors, int[] end) {
        for (int[] successor : successors) {
            if (Arrays.equals(successor, end)) {
                return true;
            }
        }
        return false;
    }

    private int indexOfList(List<int[]> list, int[] target) {
        for (int i = 0; i < list.size(); i++) {
            if (Arrays.equals(list.get(i), target)) {
                return i;
            }
        }
        return -1;
    }

    private double computeF(String heuristic, int[] parent, int[] currPos, int[] endPos) {
        double g = computeG(currPos, parent);
        double h = computeH(heuristic, currPos, endPos);
        return g + h;
    }

    private double computeG(int[] successor, int[] parent) {
        K parentNode = arr2D.get(parent[0]).get(parent[1]);
        K successorNode = arr2D.get(successor[0]).get(successor[1]);
        double edgeWeight = weights.get(parentNode).get(successorNode);
        return edgeWeight;
    }

    private double computeH(String heuristic, int[] successor, int[] goal) {
        // Manhattan distance
        if (heuristic.equals("Manhattan")) {
            return Math.abs(successor[0] - goal[0]) + Math.abs(successor[1] - goal[1]);
        }
        // Euclidean distance
        else if (heuristic.equals("Euclidean")) {
            return Math.sqrt(Math.pow(successor[0] - goal[0], 2) + Math.pow(successor[1] - goal[1], 2));
        } else if (heuristic.equals("Custom")) {
            return Math.sqrt(Math.pow(successor[0] - goal[0], 2) + Math.pow(successor[1] - goal[1], 2));
        }
        return 0.0; // default case, can add more heuristics later
    }
}

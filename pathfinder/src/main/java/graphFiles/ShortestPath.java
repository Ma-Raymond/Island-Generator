package graphFiles;

import java.net.Inet4Address;
import java.util.*;

/**
 * This class is responsible in performing dijksra's algorithm
 */
public class ShortestPath implements Path {

    Graph graph;

    /**
     * This method is responsible for the algorithm and returning a pathing list
     * @param g
     * @param node
     * @return
     */
    @Override
    public List<Integer> dijkstra(Graph g,Integer node) {
        graph = g;
        init();

        // Variables for pathing and PriorityQueue
        List<Integer> pathing = new ArrayList<>(Collections.nCopies(graph.nodeList.size(),-1));
        PriorityQueue<Node> pq = new PriorityQueue<>(graph.nodeList.size(), new Node());

        // Adding the Inital Node to branch from
        graph.nodeList.get(node).setWeight(0);
        pq.offer(graph.nodeList.get(node));

        // Dijkstra's Algorithm
        while (!pq.isEmpty()){
            Node currNode = pq.poll();
            List<Integer> edges = currNode.neighbours;
            // Loop through each neighbour
            for (int i=0; i < edges.size(); i++){
                int neighIdx = edges.get(i);    // Vertex Idx
                int newWeight = (currNode.weight + currNode.neighbourWeights.get(i));   // curr Weight + distance to get to next node
                Node neighbour = graph.nodeList.get(neighIdx);
                // If the weight is less than the one we are looking at, add to the PQ
                if (newWeight < neighbour.weight){
                    pathing.set(neighIdx,currNode.id);
                    neighbour.setWeight(newWeight);
                    pq.offer(neighbour);
                }
            }
        }
    return pathing;
    }

    /**
     * This method is responsible in initalizing the whole nodelist and updating their values to INFINITY (In this case its Interger.MAXVALUE)
     */
    private void init(){
        for(Node n : graph.nodeList){
            n.setWeight(Integer.MAX_VALUE);
        }
    }

}

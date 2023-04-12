package graphFiles;

import java.net.Inet4Address;
import java.util.*;

public class ShortestPath implements Path {

    Graph graph;
    @Override
    public List<Integer> dijkstra(Graph g,Integer node) {
        graph = g;
        init();

        List<Integer> pathing = new ArrayList<>(Collections.nCopies(graph.nodeList.size(),-1));
        PriorityQueue<Node> pq = new PriorityQueue<>(graph.nodeList.size(), new Node());

        graph.nodeList.get(node).setWeight(0);
        pq.offer(graph.nodeList.get(node));

        // Dijkstra's Algorithm
        while (!pq.isEmpty()){
            Node currNode = pq.poll();
            List<Integer> edges = currNode.neighbours;

            for (int i=0; i < edges.size(); i++){
                int neighIdx = edges.get(i);    // Vertex Idx
                int newWeight = (currNode.weight + currNode.neighbourWeights.get(i));   // curr Weight + distance to get to next node
                Node neighbour = graph.nodeList.get(neighIdx);

                if (newWeight < neighbour.weight){
                    pathing.set(neighIdx,currNode.id);
                    neighbour.setWeight(newWeight);
                    pq.offer(neighbour);
                }
            }
        }
    return pathing;
    }
    private void init(){
        for(Node n : graph.nodeList){
            n.setWeight(Integer.MAX_VALUE);
        }
    }

}

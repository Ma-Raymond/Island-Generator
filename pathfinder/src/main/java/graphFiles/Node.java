package graphFiles;

import java.util.Comparator;
import java.util.List;

public class Node implements Comparator<Node> {
    // VARAIBLES AND ATTRIBUTES OF THE NODE
    public int id;
    public int weight;
    public List<Integer> neighbours;
    public List<Integer> neighbourSegments;
    public List<Integer> neighbourWeights;
    // -----------------------------------
    public Node() {}

    // When creating a Node object, have the ID as the first intake
    public Node(int id) {
        this.id = id;
    }

    // Setting the current weight
    public void setWeight(int weight){
        this.weight = weight;
    }
    // Setting the neighbours
    public void setNeighbours(List<Integer> neighbour){
        this.neighbours = neighbour;
    }

    // Setting the neighbourSegment
    public void setNeighbourSegments(List<Integer> segs){
        this.neighbourSegments = segs;
    }
    // Set the neighbourweights
    public void setNeighbourWeights(List<Integer> neighbourWeight){
        this.neighbourWeights = neighbourWeight;
    }

    // This is to use the priority queue with a comparable override
    @Override
    public int compare(Node node1, Node node2) {
        return Integer.compare(node1.weight, node2.weight);
    }
}
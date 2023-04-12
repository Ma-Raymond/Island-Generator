package graphFiles;

import java.util.Comparator;
import java.util.List;

public class Node implements Comparator<Node> {
    public int id;
    public int weight;

    public List<Integer> neighbours;
    public List<Integer> neighbourSegments;
    public List<Integer> neighbourWeights;

    public Node() {}

    public Node(int id) {
        this.id = id;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }
    public void setNeighbours(List<Integer> neighbour){
        this.neighbours = neighbour;
    }

    public void setNeighbourSegments(List<Integer> segs){
        this.neighbourSegments = segs;
    }
    public void setNeighbourWeights(List<Integer> neighbourWeight){
        this.neighbourWeights = neighbourWeight;
    }

    @Override
    public int compare(Node node1, Node node2) {
        return Integer.compare(node1.weight, node2.weight);
    }
}
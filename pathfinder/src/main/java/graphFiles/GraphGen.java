package graphFiles;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



public class GraphGen{
    Graph meshToGraph;
    public Mesh generate(Mesh aMesh){
        meshToGraph = new Graph();
        meshToGraph.generate(aMesh);
        return Mesh.newBuilder().build();
    }

    /**
     * Get a list of segment indexes in order
     * @param dijkstras
     * @param destination
     * @return
     */
    public List<Integer> getShortestPath(List<Integer> dijkstras,Integer start, Integer destination) {
        List<Integer> path = new ArrayList<>();

        Integer value = destination;
        while (!value.equals(start)){
            Integer connect = dijkstras.get(value);
            if (connect.equals(-1)){    // This means they arent even connected, test case
                break;
            }
            path.add(getSegmentIdx(value,connect));
            value = connect;
        }
        Collections.reverse(path);
        return path;
    }
    private Integer getSegmentIdx(Integer node1, Integer node2) {
        Node node = meshToGraph.nodeList.get(node1);
        List<Integer> neighbour = node.neighbours;

        for(int i =0; i< neighbour.size(); i++){
            Integer neighIdx = neighbour.get(i);
            if (neighIdx.equals(node2)){
                return node.neighbourSegments.get(i);
            }
        }
        // Test Case for no existing segment between them
        return 0;
    }
    public List<Integer> getDijkstras(Integer node){
        if (meshToGraph != null){
            ShortestPath dijkstraList = new ShortestPath();
            return dijkstraList.dijkstra(meshToGraph,node);
        }
        // Test Case for Negatives
        return new ArrayList<>();
    }
    public Integer getWeight(Integer destination){
        if (meshToGraph != null && !(destination < 0 || destination >= meshToGraph.nodeList.size()))
            return meshToGraph.nodeList.get(destination).weight;
        // Test Case for Negatives
        return 0;
    }

}

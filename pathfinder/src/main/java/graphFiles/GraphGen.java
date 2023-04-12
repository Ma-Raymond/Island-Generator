package graphFiles;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



public class GraphGen{
    Graph meshToGraph;

    /**
     * This method is responsible of generating the generating of GraphGen
     * @param aMesh
     * @return
     */
    public Mesh generate(Mesh aMesh){
        meshToGraph = new Graph();
        meshToGraph.generate(aMesh);
        return Mesh.newBuilder().build();
    }

    /**
     * Get a list of segment indexes in order of the smallest travel distance
     * @param dijkstras
     * @param destination
     * @return
     */
    public List<Integer> getShortestPath(List<Integer> dijkstras,Integer start, Integer destination) {
        List<Integer> path = new ArrayList<>();
        // Cover invalid indexes for test case
        if ((start < 0)|| (destination < 0)|| (start >= dijkstras.size()) || destination >= dijkstras.size()){
            return new ArrayList<>();
        }
        // This finds the path needs to get to each value
        Integer value = destination;
        while (!value.equals(start)){       // While values not equal the start, if its done the loop is done
            Integer connect = dijkstras.get(value);     // Dijkstras Path Array
            if (connect.equals(-1)){        // This means they arent even connected, test case
                break;
            }
            path.add(getSegmentIdx(value,connect));     // Add to new path array
            value = connect;        // Update the value to the potential connection
        }
        Collections.reverse(path);      // As we added paths in reverse, I reversed the list back to normal
        return path;
    }

    /**
     * This method is responsible of getting the Segment ID of the Node connection
     * @param node1
     * @param node2
     * @return
     */
    private Integer getSegmentIdx(Integer node1, Integer node2) {
        Node node = meshToGraph.nodeList.get(node1);
        List<Integer> neighbour = node.neighbours;
        // LOOP THROUGH EACH NEIGHBOUR TO FIND THE CONNECTION
        for(int i =0; i< neighbour.size(); i++){
            Integer neighIdx = neighbour.get(i);
            if (neighIdx.equals(node2)){        // WHEN FOUND
                return node.neighbourSegments.get(i);       // Return segment ID corresponding to the two nodes
            }
        }
        // Test Case for no existing segment between them
        return 0;
    }

    /**
     * This method is responsible in getting the pathing list from the drijkstra method
     * @param node
     * @return
     */
    public List<Integer> getDijkstras(Integer node){
        if (meshToGraph != null){       // Safety Check for null case
            ShortestPath dijkstraList = new ShortestPath();
            return dijkstraList.dijkstra(meshToGraph,node);
        }
        // Test Case for Negatives
        return new ArrayList<>();
    }

    /**
     * This is responsible of getting the weight of the node
     * @param destination
     * @return
     */
    public Integer getWeight(Integer destination){
        if (meshToGraph != null && !(destination < 0 || destination >= meshToGraph.nodeList.size()))
            return meshToGraph.nodeList.get(destination).weight;
        // Test Case for Negatives
        return 0;
    }

}

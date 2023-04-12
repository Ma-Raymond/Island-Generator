package graphFiles;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.util.*;

/**
 * This is the general Graph to extend a more specfic graph
 */
abstract class GeneralGraph {
    HashMap<Integer, List<Integer>> adjacencyListV = new HashMap<>();
    HashMap<Integer, List<Integer>> adjacencyListS = new HashMap<>();
    HashMap<Integer, List<Integer>> weights = new HashMap<>();
}

/**
 * This the graph Class responsible to create the graph ADT and converting the mesh into a Graph with Nodes
 */
public class Graph extends GeneralGraph{
    List<Polygon> polygonList;
    List<Segment> segmentList;
    List<Vertex> vertexList;

    /**
     * This run the generation of the Graph, setting up the inital Graph from a mesh
     */
    public void generate(Mesh aMesh){
        vertexList = aMesh.getVerticesList();
        segmentList = aMesh.getSegmentsList();
        polygonList = aMesh.getPolygonsList();
        generateGraph();
        generateNodeGraph();
    }
    List<Node> nodeList = new ArrayList<>();

    /**
     * This initalizes the nodeList and responsible to create node object
     */
    private void initalizeNodes(){
        for(int i=0; i< vertexList.size(); i++){
            adjacencyListV.put(i,new ArrayList<>());
            adjacencyListS.put(i,new ArrayList<>());
            weights.put(i,new ArrayList<>());
        }
        for (int i=0; i < vertexList.size();i++){
            Node node = new Node(i);
            nodeList.add(node);     // NODE OBJECTS ADDED TO LIST
        }
    }

    /**
     * After initalizing the Node graphs, we add the mesh data into the nodeList with Node objects
     */
    private void generateNodeGraph(){
        for (int i=0; i< nodeList.size();i++){
            Node node = nodeList.get(i);
            node.setNeighbours(adjacencyListV.get(i));      // NEIGHBOURS
            node.setNeighbourSegments(adjacencyListS.get(i));       // SEGMENT IDS
            node.setNeighbourWeights(weights.get(i));       // WEIGHTS ID
        }
    }

    /**
     * This method is responsible to the conversion of mesh data into variables to be used later to generate the Graph ADT
     */
    private void generateGraph(){
        initalizeNodes();
        for (Segment s : segmentList){      // Every Segment
            int v1Idx = s.getV1Idx();
            int v2Idx = s.getV2Idx();
            int segID = segmentList.indexOf(s);
            // CHECK FOR EACH VERTEX AND SEE IF THEY ARE ACCOUNTED FOR IN DATA
            if (!adjacencyListV.get(v1Idx).contains(v2Idx)){
                // ADD INTO Vertex Adjacency List
                List<Integer> vList = adjacencyListV.get(v1Idx);
                vList.add(v2Idx);
                adjacencyListV.put(v1Idx,vList);

                // ADD INTO Segment Adjacency List
                List<Integer> sList = adjacencyListS.get(v1Idx);
                sList.add(segID);
                adjacencyListS.put(v1Idx,sList);

                // ADD INTO Weight Adjacency List
                List<Integer> wList = weights.get(v1Idx);
                wList.add(generateWeight(v1Idx,v2Idx));
                weights.put(v1Idx,wList);
            }
            // CHECK FOR EACH VERTEX AND SEE IF THEY ARE ACCOUNTED FOR IN DATA BUT REVERSED ORDER TO CHECK
            if (!adjacencyListV.get(v2Idx).contains(v1Idx)){
                // ADD INTO Vertex Adjacency List
                List<Integer> vList = adjacencyListV.get(v2Idx);
                vList.add(v1Idx);
                adjacencyListV.put(v2Idx,vList);

                // ADD INTO Segment Adjacency List
                List<Integer> sList = adjacencyListS.get(v2Idx);
                sList.add(segID);
                adjacencyListS.put(v2Idx,sList);

                // ADD INTO Weight Adjacency List
                List<Integer> wList = weights.get(v2Idx);
                wList.add(generateWeight(v2Idx,v1Idx));
                weights.put(v2Idx,wList);
            }
        }
    }

    /**
     * This method is responsible to generating the weight for the segments based on their distance x and y
     * @param coreIdx
     * @param destinationIdx
     * @return
     */
    private Integer generateWeight(Integer coreIdx, Integer destinationIdx){
        // X AND Y FOR VERTEX 1
        Double x1 = vertexList.get(coreIdx).getX();
        Double y1 = vertexList.get(coreIdx).getY();

        // X AND Y FOR VERTEX 2
        Double x2 = vertexList.get(destinationIdx).getX();
        Double y2 = vertexList.get(destinationIdx).getY();

        return (int)Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
}

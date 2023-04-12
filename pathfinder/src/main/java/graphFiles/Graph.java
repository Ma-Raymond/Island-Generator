package graphFiles;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.util.*;

abstract class GeneralGraph {
    HashMap<Integer, List<Integer>> adjacencyListV = new HashMap<>();
    HashMap<Integer, List<Integer>> adjacencyListS = new HashMap<>();
    HashMap<Integer, List<Integer>> weights = new HashMap<>();
}

public class Graph extends GeneralGraph{
    List<Polygon> polygonList;
    List<Segment> segmentList;
    List<Vertex> vertexList;

    public Mesh generate(Mesh aMesh){
        vertexList = aMesh.getVerticesList();
        segmentList = aMesh.getSegmentsList();
        polygonList = aMesh.getPolygonsList();
        generateGraph();
        generateNodeGraph();
        return Mesh.newBuilder().build();
    }
    List<Node> nodeList = new ArrayList<>();
    private void initalizeNodes(){
        for(int i=0; i< vertexList.size(); i++){
            adjacencyListV.put(i,new ArrayList<>());
            adjacencyListS.put(i,new ArrayList<>());
            weights.put(i,new ArrayList<>());
        }
        for (int i=0; i < vertexList.size();i++){
            Node node = new Node(i);
            nodeList.add(node);
        }
    }
    public void generateNodeGraph(){
        for (int i=0; i< nodeList.size();i++){
            Node node = nodeList.get(i);
            node.setNeighbours(adjacencyListV.get(i));
            node.setNeighbourSegments(adjacencyListS.get(i));
            node.setNeighbourWeights(weights.get(i));
        }
    }
    public void generateGraph(){
        initalizeNodes();
        for (Segment s : segmentList){
            int v1Idx = s.getV1Idx();
            int v2Idx = s.getV2Idx();
            int segID = segmentList.indexOf(s);

            if (!adjacencyListV.get(v1Idx).contains(v2Idx)){
                List<Integer> vList = adjacencyListV.get(v1Idx);
                vList.add(v2Idx);
                adjacencyListV.put(v1Idx,vList);

                List<Integer> sList = adjacencyListS.get(v1Idx);
                sList.add(segID);
                adjacencyListS.put(v1Idx,sList);

                List<Integer> wList = weights.get(v1Idx);
                wList.add(generateWeight(v1Idx,v2Idx));
                weights.put(v1Idx,wList);
            }
            if (!adjacencyListV.get(v2Idx).contains(v1Idx)){
                List<Integer> vList = adjacencyListV.get(v2Idx);
                vList.add(v1Idx);
                adjacencyListV.put(v2Idx,vList);

                List<Integer> sList = adjacencyListS.get(v2Idx);
                sList.add(segID);
                adjacencyListS.put(v2Idx,sList);

                List<Integer> wList = weights.get(v2Idx);
                wList.add(generateWeight(v2Idx,v1Idx));
                weights.put(v2Idx,wList);
            }
        }
    }
    private Integer generateWeight(Integer coreIdx, Integer destinationIdx){
        Double x1 = vertexList.get(coreIdx).getX();
        Double y1 = vertexList.get(coreIdx).getY();

        Double x2 = vertexList.get(destinationIdx).getX();
        Double y2 = vertexList.get(destinationIdx).getY();

        return (int)Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
}

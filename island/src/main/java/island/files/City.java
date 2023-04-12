package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import graphFiles.GraphGen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class City {

    List<Structs.Segment> segmentList;
    List<Structs.Vertex> vertexList;
    GraphGen graph;
    public void generate(Structs.Mesh aMesh,List<Integer> lakeIdxs, List<Integer> islandVertices, int numCity, int cityStartIdx, List<Structs.Segment> sList, List<Structs.Vertex> vList, List<Structs.Polygon> polygonList) {
        graph = new GraphGen();
        graph.generate(aMesh);

        vertexList = vList;
        segmentList = sList;
        List<Integer> spawnPoints = cityAvaliableSpawn(lakeIdxs,islandVertices,polygonList);
        addCities(spawnPoints,numCity,cityStartIdx);
        generateStarNetwork();
    }
    List<Integer> cityLocations = new ArrayList<>();
    private void addCities(List<Integer> spawnPoints, int numCity,int cityStartIdx){
        int val = cityStartIdx;
        for (int i =0; i < numCity; i++){
            val += 1;
            Integer vertIdx = spawnPoints.get(val);
            cityLocations.add(vertIdx);
            colorVertex(vertIdx,1,1,1,255);
            double sizeVariant = 5.0 + val%5;
            sizeCity(vertIdx,sizeVariant);
        }
    }
    private void generateStarNetwork() {
        Integer lowestValue = null;
        Integer centralIdx = null;
        for (Integer idx : cityLocations){
            graph.getDijkstras(idx);
            Integer highestValue = null;
            for (Integer compareIdx : cityLocations){
                if (!idx.equals(compareIdx)){
                    if ((highestValue == null) || graph.getWeight(compareIdx) < highestValue){
                        highestValue = graph.getWeight(compareIdx);
                    }
                }
            }
            if ((centralIdx == null) || highestValue < lowestValue ){
                lowestValue = highestValue;
                centralIdx = idx;
            }
        }

        List<Integer> dijkPath = graph.getDijkstras(centralIdx);
        colorVertex(centralIdx,255,215,0,255); // GOLD CENTRALL
        new Color(255,215,0);
        for (Integer idx : cityLocations){
            if (!idx.equals(centralIdx)){
                List<Integer> path = graph.getShortestPath(dijkPath,centralIdx,idx);
                for (Integer segId : path){
                    colorSegment(segId,1,1,1,255);
                }
            }
        }
    }

    private List<Integer> cityAvaliableSpawn(List<Integer> lakeIdxs, List<Integer> islandVertices, List<Structs.Polygon> polygonList){
        List<Integer> lakeVertices = new ArrayList<>();
        for (Integer i : lakeIdxs){
            lakeVertices.addAll(Objects.requireNonNull(extractVertices(polygonList.get(i).getPropertiesList())));
        }
        return islandVertices.stream().filter(e -> !lakeVertices.contains(e)).collect(Collectors.toList());
    }
    private List<Integer> extractVertices(List<Structs.Property> properties){
        String val = null;
        for(Structs.Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("vertices")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            System.out.println("NO VERTEX PROPERTY");
            return null;
        }
        String[] raw = val.split(",");
        List<Integer> rawInts = new ArrayList<>();
        for (int i =0; i< raw.length;i++){
            Integer value = Integer.parseInt(raw[i]);
            rawInts.add(value);
        }
        return rawInts;
    }
    private void colorVertex(Integer vertexId, int red, int green, int blue, int alpha){
        String colorCode = red + "," + green + "," + blue + "," + alpha;
        Structs.Vertex vertex = vertexList.get(vertexId);
        // Create new Property with "rgb_color" key and the rgb value as the value
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        Structs.Vertex colored = Structs.Vertex.newBuilder(vertex).addProperties(color).build();
        // Set the old vertex in the list as the new one with color property
        vertexList.set(vertexId, colored);
    }
    private void sizeCity(Integer vertexId, double citySize){
        String citySizeString = String.valueOf(citySize);
        Structs.Vertex vertex = vertexList.get(vertexId);
        // Create new Property with "rgb_color" key and the rgb value as the value
        Structs.Property color = Structs.Property.newBuilder().setKey("citySize").setValue(citySizeString).build();
        Structs.Vertex citySized = Structs.Vertex.newBuilder(vertex).addProperties(color).build();
        // Set the old vertex in the list as the new one with color property
        vertexList.set(vertexId, citySized);
    }
    private void colorSegment(Integer segId, int red, int green, int blue, int alpha){
        // Create new Property with "rgb_color" key and the rgb value as the value
        Structs.Segment seg = segmentList.get(segId);
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Structs.Segment colored = Structs.Segment.newBuilder(seg).addProperties(color).build();
        // Set the old segment in the list as the new one with color property
        segmentList.set(segId, colored);
    }

}

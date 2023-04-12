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

    /**
     *
     * This method is responsible in generating the cities onto the Island
     */
    public void generate(Structs.Mesh aMesh,List<Integer> lakeIdxs, List<Integer> islandVertices, int numCity, int cityStartIdx, List<Structs.Segment> sList, List<Structs.Vertex> vList, List<Structs.Polygon> polygonList) {
        graph = new GraphGen();
        graph.generate(aMesh);

        vertexList = vList;
        segmentList = sList;
        //
        List<Integer> spawnPoints = cityAvaliableSpawn(lakeIdxs,islandVertices,polygonList);
        addCities(spawnPoints,numCity,cityStartIdx);
        generateStarNetwork();
    }
    List<Integer> cityLocations = new ArrayList<>();

    /**
     * This method is responsible in creating cities in the map and colouring them black
     */
    private void addCities(List<Integer> spawnPoints, int numCity,int cityStartIdx){
        int val = cityStartIdx;
        // Loops through each city
        for (int i =0; i < numCity; i++){
            val += 1;
            Integer vertIdx = spawnPoints.get(val); // Get the vertex ID
            cityLocations.add(vertIdx);             // Add the vertexID into the city Locations to keep track
            colorVertex(vertIdx,1,1,1,255);     // Colouring
            double sizeVariant = 5.0 + val%5;       // This is the randomized size
            sizeCity(vertIdx,sizeVariant);          // This sized the city accordingly
        }
    }

    /**
     * This method is responsible for creating the star network based on the city locations
     */
    private void generateStarNetwork() {
        Integer lowestValue = null;
        Integer centralIdx = null;
        // LOOPS THROUGH EACH CITY LOCATION
        for (Integer idx : cityLocations){
            graph.getDijkstras(idx);
            Integer highestValue = null;
            // Loop through each city to check their distance to get the maximum value.
            for (Integer compareIdx : cityLocations){
                if (!idx.equals(compareIdx)){
                    if ((highestValue == null) || graph.getWeight(compareIdx) < highestValue){     // Finding the smallest distance the max value is
                        highestValue = graph.getWeight(compareIdx);
                    }
                }
            }
            // If this node is more central than the previous one, updating accordingly
            if ((centralIdx == null) || highestValue < lowestValue ){
                lowestValue = highestValue;
                centralIdx = idx;       // Update the centralIdx accordingly
            }
        }
        // COLOUR ALL THE PATHS AND colour the central city golden.
        if (cityLocations.size() > 0){
            List<Integer> dijkPath = graph.getDijkstras(centralIdx);
            colorVertex(centralIdx,255,215,0,255); // GOLD CENTRALL
            new Color(255,215,0);
            for (Integer idx : cityLocations){      // LOOP THROUGH EACH CITY AND COLOUR PATH TO IT.
                if (!idx.equals(centralIdx)){
                    List<Integer> path = graph.getShortestPath(dijkPath,centralIdx,idx);        // PATH list with segmentIDs
                    for (Integer segId : path){
                        colorSegment(segId,1,1,1,255);      // Colour black
                    }
                }
            }
        }
    }

    /**
     *
     * This method is responsible in creating a list avaliable to use to spawn a city.
     * The reason behind it is because I dont want to spawn a City on a lake.
     */
    private List<Integer> cityAvaliableSpawn(List<Integer> lakeIdxs, List<Integer> islandVertices, List<Structs.Polygon> polygonList){
        List<Integer> lakeVertices = new ArrayList<>();
        for (Integer i : lakeIdxs){
            lakeVertices.addAll(Objects.requireNonNull(extractVertices(polygonList.get(i).getPropertiesList())));
        }
        // RETURN A Filtered array without the lakeVertices
        return islandVertices.stream().filter(e -> !lakeVertices.contains(e)).collect(Collectors.toList());
    }

    /**
     * This method is responsible in getting the vertices and return an Arraylist
     * @param properties
     * @return List<Integer>
     */
    private List<Integer> extractVertices(List<Structs.Property> properties){
        String val = null;
        for(Structs.Property p: properties) {
            // TRY TO FIND THE VERTICES
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
        return rawInts;     // Return the vertices in an ArrayList
    }

    /**
     * This method is responsible in getting colouring the vertex
     */
    private void colorVertex(Integer vertexId, int red, int green, int blue, int alpha){
        String colorCode = red + "," + green + "," + blue + "," + alpha;
        Structs.Vertex vertex = vertexList.get(vertexId);
        // Create new Property with "rgb_color" key and the rgb value as the value
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        Structs.Vertex colored = Structs.Vertex.newBuilder(vertex).addProperties(color).build();
        // Set the old vertex in the list as the new one with color property
        vertexList.set(vertexId, colored);
    }
    /**
     *
     * This method is responsible for generating the sizes of the city
     */
    private void sizeCity(Integer vertexId, double citySize){
        String citySizeString = String.valueOf(citySize);
        Structs.Vertex vertex = vertexList.get(vertexId);
        // Create new Property with "citySize" key and the rgb value as the value
        Structs.Property color = Structs.Property.newBuilder().setKey("citySize").setValue(citySizeString).build();
        Structs.Vertex citySized = Structs.Vertex.newBuilder(vertex).addProperties(color).build();
        // Set the old vertex in the list as the new one with sized property
        vertexList.set(vertexId, citySized);
    }
    /**
     * This method is responsible in getting colouring the segment
     */
    private void colorSegment(Integer segId, int red, int green, int blue, int alpha){
        // Create new Property with "rgb_color" key and the rgb value as the value
        Structs.Segment seg = segmentList.get(segId);
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Structs.Segment colored = Structs.Segment.newBuilder(seg).addProperties(color).build();
        // Set the old segment in the list as the new one with color property
        segmentList.set(segId, colored);
    }

}

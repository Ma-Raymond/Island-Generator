package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


abstract class IslandSeed{
    int shape;
    int AltType;
    int AltNum;
    int lakeNum;
    int lakeStartIdx;
    int riverNum;
    int riverStartIdx;
    int aquaNum;
    int aquaStartIdx;
    int soil;
    int biome;

}

public class IslandGen extends IslandSeed {

    List<Polygon> polygonList;
    List<Segment> segmentList;
    List<Vertex> vertexList;
    List<Double> elevations;
    List<Double> humidity;

    String islandColor = "253,255,208,255";
    List<Integer> islandBlocks = new ArrayList<>();
    List<Integer> heightPoints = new ArrayList<>();

    private void circleIsland(Mesh aMesh){
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double distance = Math.sqrt(Math.pow(x-255,2)+Math.pow(y-255,2));
            //lake
//            if (distance < 100){
//                colorPolygon(poly, 102, 178,255,255);
//            }
            //island
            if (distance < 200){
                colorPolygon(poly, 253, 255,208,255);
            }
            else{
                colorPolygon(poly, 35, 85,138,255);
            }
        }
    }

    private void createLakes(Mesh aMesh, int maxLakes){
        Random startLake = new Random();
        int startIndexL = startLake.nextInt(heightPoints.size());
        Random randLakeNum = new Random();
        int numLakes = randLakeNum.nextInt(1,maxLakes);
        for (int i = 0; i < numLakes; i ++){
            int polyIndex = (startIndexL + i) % heightPoints.size();
            int validPolyId  = heightPoints.get(polyIndex);
            Polygon poly = polygonList.get(validPolyId);
            colorPolygon(poly, 102, 178,255,255);

       }
        lakeStartIdx = startIndexL;
    }
    private void addLakeHumidity(){

    }


    /**
     * Generate the new Islands
     * @param aMesh
     * @return
     */
    public Mesh generate(Mesh aMesh,String shape){
        // Get old mesh details
        polygonList = new ArrayList<>(aMesh.getPolygonsList());
        segmentList = new ArrayList<>(aMesh.getSegmentsList());
        vertexList = new ArrayList<>(aMesh.getVerticesList());

        // Set new Stats
        int nPolygons = polygonList.size();
        elevations = new ArrayList<Double>(Collections.nCopies(nPolygons, 1.0));
        humidity = new ArrayList<Double>(Collections.nCopies(nPolygons, 100.0));

        // New Island Meshes
        circleIsland(aMesh);


        // Get Island Blocks
        getIslandBlocks();

        // Generate Elevation
        generateElevation();
        createLakes(aMesh, 100);

        // Assigning Biomes and Types

        return Mesh.newBuilder().addAllVertices(vertexList).addAllSegments(segmentList).addAllPolygons(polygonList).build();
    }

    private void getIslandBlocks(){
        for (int i = 0; i < polygonList.size();i++){
            Polygon poly = polygonList.get(i);
            if (extractColorString(poly.getPropertiesList()).equals(islandColor)){
                islandBlocks.add(i);
            }
        }
        Collections.shuffle(islandBlocks,new Random(2));
    }
    private void generateElevation(){

        for (Integer polyIdx : islandBlocks){
            boolean allNeighbourIslands = true;
            Polygon poly = polygonList.get(polyIdx);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            for (Integer j : neighbourList){
                if (!islandBlocks.contains(j)){
                    allNeighbourIslands = false;
                }
            }
            if (allNeighbourIslands){
                heightPoints.add(polyIdx);
            }
        }


        // Have it incrementally do it with the seed
        Random rand = new Random();
        for (int i = 0; i < rand.nextInt(1,4); i++){
            int randIdx = rand.nextInt(heightPoints.size());
            int polyIdx = heightPoints.get(randIdx);
            Polygon poly = polygonList.get(polyIdx);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            colorHeight(poly,1.5);
            elevations.set(polyIdx,1.5);
            for (Integer j : neighbourList){
                Polygon neighbourPoly = polygonList.get(j);
                colorHeight(neighbourPoly,1.2);
                elevations.set(j,1.2);
            }
        }
    }
    private void colorHeight(Polygon poly, double value){
        // Island is "253,255,208,255"
        double red = 253/value;
        double green = 255/value;
        double blue = 208/value;
        colorPolygon(poly,(int)red,(int)green,(int)blue,255);
    }
    private void colorPolygon(Polygon poly, int red, int green, int blue, int alpha){
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Polygon colored = Polygon.newBuilder(poly).addProperties(color).build();
        polygonList.set(polygonList.indexOf(poly), colored);
    }
    private void assignType(Polygon poly, String type){
        Structs.Property typeProperty = Structs.Property.newBuilder().setKey("Type").setValue(type).build();
        Polygon typed = Polygon.newBuilder(poly).addProperties(typeProperty).build();
        polygonList.set(polygonList.indexOf(poly), typed);
    }
    private String extractType(List<Structs.Property> properties){
        String val = null;
        for(Structs.Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("Type")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            return "None"; // COVERING CASE IF KEY RGB_COLOR DOESN'T EXIST
        }
        return val;
    }
    private String extractColorString(List<Structs.Property> properties){
        String val = null;

        for(Structs.Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            return "0,0,0,0"; // COVERING CASE IF KEY RGB_COLOR DOESN'T EXIST
        }
        return val;
    }
    private Color extractColor(List<Structs.Property> properties) {
        String val = null;
        // EXTRACTCOLOR GOES THROUGH ALL THE PROPERTIES OF THE OBJECT
        for(Structs.Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            return Color.BLACK; // COVERING CASE IF KEY RGB_COLOR DOESN'T EXIST
        }
        // IF RGB PROPERTY EXIST, GET VALUES OF EACH PARAMETER
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        int alpha = Integer.parseInt(raw[3]);
        // RETURN AS COLOR OBJECT
        return new Color(red, green, blue, alpha);
    }
}

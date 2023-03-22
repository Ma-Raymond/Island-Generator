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

public class IslandGen {
    List<Polygon> polygonList;
    List<Segment> segmentList;
    List<Vertex> vertexList;
    List<Double> elevations;
    String islandColor = "253,255,208,255";
    List<Integer> islandBlocks = new ArrayList<>();

    private void circleIsland(Mesh aMesh){
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double distance = Math.sqrt(Math.pow(x-255,2)+Math.pow(y-255,2));
            if (distance < 100){
                colorPolygon(poly, 102, 178,255,255);
            }
            else if (distance < 200){
                colorPolygon(poly, 253, 255,208,255);
            }
            else{
                colorPolygon(poly, 35, 85,138,255);
            }
        }
    }
    private void waveyIsland(Mesh aMesh){
        Random bag = new Random();
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double distance = Math.sqrt(Math.pow(x-250,2)+Math.pow(y-250,2));

            double distance1 = Math.sqrt(Math.pow(x-450,2)+Math.pow(y-250,2));

            if (distance < 200 && distance1 > 100){
                colorPolygon(poly, 253, 255,208,255);
            }
            else{
                colorPolygon(poly, 35, 85,138,255);
            }
        }
    }
    private void ovalIsland(Mesh aMesh){
        Random bag = new Random();
        int a = bag.nextInt(100, 200);
        int b = bag.nextInt(50, 150);
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double result = Math.pow(((x-250)/a),2) + Math.pow(((y-250)/b),2) -1;

            if (result < 0) {
                colorPolygon(poly, 253, 255, 208, 255);
            }

            else{
                colorPolygon(poly, 35, 85,138,255);
            }

        }

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
        // New Island Meshes
        waveyIsland(aMesh);

        // Get Island Blocks
        getIslandBlocks();

        // Generate Elevation
        generateElevation();

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
    }
    private void generateElevation(){
        List<Integer> heightPoints = new ArrayList<>();
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

package ca.mcmaster.cas.se2aa4.a2.generator;
import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import java.awt.*;
import java.util.*;
import java.util.List;


abstract class GeneralMesh {
    Mesh myMesh;
    public final int width = 500;
    public final int height = 500;
    public final int square_size = 20;
    Set<Vertex> vertices = new HashSet<>();
    Set<Segment> segments = new HashSet<>();

    List<Vertex> vertexList = new ArrayList<Vertex>();
    List<Segment> segmentList = new ArrayList<Segment>();
    List<Polygon> polygonList = new ArrayList<Polygon>();
}
public class MeshGen extends GeneralMesh{

    public void makeVertex(){
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {
                // Here I replicate a square with vertices
                Vertex v1 = Vertex.newBuilder().setX((double) x).setY((double) y).build();      // Top left
                Vertex v2 = Vertex.newBuilder().setX((double) x + square_size).setY((double) y).build();    // Top Right
                Vertex v3 = Vertex.newBuilder().setX((double) x).setY((double) y + square_size).build();    // Bottom Left
                Vertex v4 = Vertex.newBuilder().setX((double) x+square_size).setY((double) y + square_size).build();    // Bottom Right
                // This list is made so I can conveniently run through each vertex in the square
                List<Vertex> square = new ArrayList<Vertex>(Arrays.asList(v1,v2,v3,v4));
                for (Vertex v : square){
                    if (!vertices.contains(v)){     // If it's not in the SET, add it and also add it to the Iterable List vertexList
                        vertices.add(v);            // The SET will prevent duplicates
                        vertexList.add(v);
                    }
                }
                // This function will create the segments and define the polygons of the square based on the vertexes made here
                makePolygon(vertexList.indexOf(v1),vertexList.indexOf(v2),vertexList.indexOf(v3),vertexList.indexOf(v4));
            }
        }
    }
    public void makePolygon(int v1,int v2,int v3,int v4){
        // Created Segments here based on the identifier of the vertexes to make a square shape
        Segment s1 = Segment.newBuilder().setV1Idx(v1).setV2Idx(v2).build();
        Segment s2 = Segment.newBuilder().setV1Idx(v2).setV2Idx(v4).build();
        Segment s3 = Segment.newBuilder().setV1Idx(v3).setV2Idx(v4).build();
        Segment s4 = Segment.newBuilder().setV1Idx(v1).setV2Idx(v3).build();

        // This list is made so I can conviently run through each segment in the square
        List<Segment> square = new ArrayList<Segment>(Arrays.asList(s1,s2,s3,s4));
        for (Segment s : square){
            if (!segments.contains(s)){     // If it's not in the SET, add it and also add it to the Iterable List
                segments.add(s);            // The SET will prevent duplicates
                segmentList.add(s);
            }
        }
        // Here I created an integer list of all the segment identifiers, to make up the square. It is added in a consecutive order aswell
        List<Integer> squarelist = new ArrayList<Integer>(Arrays.asList(segmentList.indexOf(s1),segmentList.indexOf(s2),segmentList.indexOf(s3),segmentList.indexOf(s4)));

        // Creates a polygon object, adds the list of integers of segments to the object
        Polygon poly = Polygon.newBuilder().addAllSegmentIdxs(squarelist).build();
        polygonList.add(poly);      // Add the polygon object into the polygonList

        // Gayan will make centroids
        Vertex centroid;
        System.out.println(poly);
    }


    public Mesh generate(String Mode) {
        // This will make the vertexes
        makeVertex();
        List<Vertex> verticesWithColors = new ArrayList<>();
        Set<Segment> segmentsWithColors = new HashSet<>();

        //not finished
        if (Mode.equals("-X")) {
            for (Vertex v : vertexList){

            }


        } else {

            Random bag = new Random();
            for (Vertex v : vertexList) {
                int red = bag.nextInt(255);
                int green = bag.nextInt(255);
                int blue = bag.nextInt(255);
                String colorCode = red + "," + green + "," + blue;
                Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
                Vertex colored = Vertex.newBuilder(v).addProperties(color).build();
                verticesWithColors.add(colored);
            }

            //Coloring the Segments

            ArrayList<Vertex> vertexToVertexMap = new ArrayList<Vertex>();
            vertexToVertexMap.addAll(verticesWithColors);
            for (Segment s : segmentList) {
//            System.out.println("V1: " + extractColor(vertexToVertexMap.get(s.getV1Idx()).getPropertiesList()) );
//            System.out.println("V2: " + extractColor(vertexToVertexMap.get(s.getV2Idx()).getPropertiesList()) );
                Color v1Color = extractColor(vertexToVertexMap.get(s.getV1Idx()).getPropertiesList());
                Color v2Color = extractColor(vertexToVertexMap.get(s.getV2Idx()).getPropertiesList());
                int red = (v1Color.getRed() + v2Color.getRed()) / 2;
                int green = (v1Color.getGreen() + v2Color.getGreen()) / 2;
                int blue = (v1Color.getBlue() + v2Color.getBlue()) / 2;
//            Color segmentColour = new Color((v1Color.getRed()+v2Color.getRed())/2,(v1Color.getGreen()+v2Color.getGreen())/2, (v1Color.getBlue()+v2Color.getBlue())/2);
                Property color = Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue).build();
                Segment colored = Segment.newBuilder(s).addProperties(color).build();
                segmentsWithColors.add(colored);
            }

        }
        return Mesh.newBuilder().addAllVertices(verticesWithColors).addAllSegments(segmentsWithColors).build();

    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);

    }

    // Rhea will colour in the vertices and segments

}

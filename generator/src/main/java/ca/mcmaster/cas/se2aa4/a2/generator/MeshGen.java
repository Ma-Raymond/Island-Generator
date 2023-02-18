package ca.mcmaster.cas.se2aa4.a2.generator;
import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;

import java.util.*;


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
                // This list is made so I can conviently run through each vertex in the square
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

//        for(Polygon poly: polygonList){
//            poly.getPropertiesList();
//
//        }
    }
    public void makePolygon(int v1Id,int v2Id,int v3Id,int v4Id){
        // Created Segments here based on the identifier of the vertexes to make a square shape
        Segment s1 = Segment.newBuilder().setV1Idx(v1Id).setV2Idx(v2Id).build();
        Segment s2 = Segment.newBuilder().setV1Idx(v2Id).setV2Idx(v4Id).build();
        Segment s3 = Segment.newBuilder().setV1Idx(v3Id).setV2Idx(v4Id).build();
        Segment s4 = Segment.newBuilder().setV1Idx(v1Id).setV2Idx(v3Id).build();

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



        // Gayan will make centroids
        Vertex vertex1 = vertexList.get(v1Id);
        Vertex vertex2 = vertexList.get(v2Id);
        Vertex vertex3 = vertexList.get(v3Id);


        double centroidIdx = (double)(vertex1.getX()+vertex2.getX())/2;
        double centroidIdy = (double)(vertex1.getY()+vertex3.getY())/2;
        Vertex centroid = Vertex.newBuilder().setX(centroidIdx).setY(centroidIdy).build();
        vertexList.add(centroid);
        Property vertices = Property.newBuilder().setKey("vertices").setValue(v1Id + "," + v2Id + "," + v3Id + "," + v4Id).build();
        // Creates a polygon object, adds the list of integers of segments to the object
        Polygon poly = Polygon.newBuilder().addAllSegmentIdxs(squarelist).setCentroidIdx(vertexList.indexOf(centroid)).addProperties(vertices).build();
        polygonList.add(poly);      // Add the polygon object into the polygonList
        System.out.println(poly);
    }
    public Mesh generate(){
        // This will make the vertexes
        makeVertex();
        return Mesh.newBuilder().addAllVertices(vertexList).addAllPolygons(polygonList).addAllSegments(segmentList).build();
    }

    // Gayan will colour in the vertices and segments
    public void colourVertices(){

    }
}

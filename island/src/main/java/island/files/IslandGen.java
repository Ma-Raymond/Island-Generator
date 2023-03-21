package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IslandGen {
    List<Polygon> polygonList;
    List<Segment> segmentList;
    List<Vertex> vertexList;


    /**
     * Generate the new Islands
     * @param aMesh
     * @return
     */
    public Mesh generate(Mesh aMesh){
        polygonList = new ArrayList<>(aMesh.getPolygonsList());
        segmentList = new ArrayList<>(aMesh.getSegmentsList());
        vertexList = new ArrayList<>(aMesh.getVerticesList());
        Random rand = new Random();
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            colorPolygon(poly, rand.nextInt(255), rand.nextInt(255),rand.nextInt(255),255);
        }
        return Mesh.newBuilder().addAllVertices(vertexList).addAllSegments(segmentList).addAllPolygons(polygonList).build();
    }
    private void colorPolygon(Polygon poly, int red, int green, int blue, int alpha){
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Polygon colored = Polygon.newBuilder(poly).addProperties(color).build();
        polygonList.set(polygonList.indexOf(poly), colored);
    }
}

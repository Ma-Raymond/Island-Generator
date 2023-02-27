package ca.mcmaster.cas.se2aa4.a2.generator;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

//FROM JTS
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.awt.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


// This is Part 2 using a more Object-Oriented Approach
abstract class GeneralMesh {
    //THESE DATASETS ARE FOR REGULAR MESH
    public int WIDTH = 500;
    public int HEIGHT = 500;
    Set<Vertex> vertices = new HashSet<>();
    Set<Segment> segments = new HashSet<>();

    List<Vertex> vertexList = new ArrayList<Vertex>();
    List<Segment> segmentList = new ArrayList<Segment>();
    List<Polygon> polygonList = new ArrayList<Polygon>();
    List<Vertex> centroidList = new ArrayList<>();
    List<Segment> neighbourConnectionList = new ArrayList<>();

    DecimalFormat precision  = new DecimalFormat("0.00");
}

public class MeshGen extends GeneralMesh{
    public final int SQUARE_SIZE = 20;
    public void makeVertex(){
        Set<Integer> neighbourConnectionsPropertyList = new HashSet<>();
        for(int x = 0; x < WIDTH; x += SQUARE_SIZE) {
            for(int y = 0; y < HEIGHT; y += SQUARE_SIZE) {
                // Here I replicate a square with vertices
                Vertex v1 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x))).setY(Double.parseDouble(precision.format(y))).build();      // Top left
                Vertex v2 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x + SQUARE_SIZE))).setY(Double.parseDouble(precision.format(y))).build();    // Top Right
                Vertex v3 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x))).setY(Double.parseDouble(precision.format(y + SQUARE_SIZE))).build();    // Bottom Left
                Vertex v4 = Vertex.newBuilder().setX( Double.parseDouble(precision.format(x+ SQUARE_SIZE))).setY(Double.parseDouble(precision.format(y + SQUARE_SIZE))).build();    // Bottom Right
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


        Set<Integer> neighboursList = new HashSet<>();
        for(Polygon poly: polygonList){
            String val = poly.getPropertiesList().get(0).getValue();
            String[] raw = val.split(",");
            for(int i = 0; i<polygonList.size(); i++){
                Polygon polyCompare = polygonList.get(i);
                String valCompare = polyCompare.getPropertiesList().get(0).getValue();
                String[] rawCompare = valCompare.split(",");
                for (int j = 0; j<4; j++){
                    int vertex = Integer.parseInt(raw[j]);
                    for (int y = 0; y<4; y++){
                        int vertexCompare = Integer.parseInt(rawCompare[y]);
                        if (vertex == vertexCompare && (poly.getCentroidIdx() != polyCompare.getCentroidIdx())){
                            neighboursList.add(i);

                        }
                    }

                }
            }
            Property neighbours = Property.newBuilder().setKey("neighbours").setValue(String.valueOf(neighboursList)).build();
            Polygon polyNew = Polygon.newBuilder(poly).addAllNeighborIdxs(neighboursList).build();
            polygonList.set(polygonList.indexOf(poly), polyNew);
            neighboursList.clear();
        }


        for (Polygon poly:polygonList){
//            System.out.println(poly);
            for (Integer i : poly.getNeighborIdxsList()){
                Segment neighbourConnection = Segment.newBuilder().setV1Idx(poly.getCentroidIdx()).setV2Idx(polygonList.get(i).getCentroidIdx()).build();

                System.out.println(neighbourConnection);
                if (!segmentList.contains(neighbourConnection)){
                    segmentList.add(neighbourConnection);
                    neighbourConnectionList.add(neighbourConnection);
                }
                neighbourConnectionsPropertyList.add(segmentList.indexOf(neighbourConnection));
            }

            Property neighbourConnections = Property.newBuilder().setKey("neighbourConnections").setValue(String.valueOf(neighbourConnectionsPropertyList)).build();
            Polygon polyNew = Polygon.newBuilder(poly).addProperties(neighbourConnections).build();
            polygonList.set(polygonList.indexOf(poly), polyNew);
            neighbourConnectionsPropertyList.clear();
        }
        System.out.println(polygonList);

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


        double centroidIdx = Double.parseDouble(precision.format((vertex1.getX()+vertex2.getX())/2));
        double centroidIdy = Double.parseDouble(precision.format((vertex1.getY()+vertex3.getY())/2));
        System.out.println(centroidIdx);
        System.out.println(centroidIdy);
        Vertex centroid = Vertex.newBuilder().setX(centroidIdx).setY(centroidIdy).build();
        vertexList.add(centroid);
        centroidList.add(centroid);
        Property vertices = Property.newBuilder().setKey("vertices").setValue(v1Id + "," + v2Id + "," + v3Id + "," + v4Id).build();
        // Creates a polygon object, adds the list of integers of segments to the object
        Polygon poly = Polygon.newBuilder().addAllSegmentIdxs(squarelist).setCentroidIdx(vertexList.indexOf(centroid)).addProperties(vertices).build();
        polygonList.add(poly);      // Add the polygon object into the polygonList
        System.out.println(poly);
    }




    public Mesh generate(String Mode) {

        // This will make the vertexes
        makeVertex();


        if (Mode.equals("true")) {
            for (Vertex v : vertexList){
                int red = 0;
                int green = 0;
                int blue = 0;
                int alpha = 255;
                if (centroidList.contains(v)){
                    red = 255;
                }
                colorVertex(v, red, green, blue,alpha);
            }


            //Make segments black
            for (Segment s : segmentList){
                int red = 0;
                int green = 0;
                int blue = 0;
                int alpha = 255;
                if (neighbourConnectionList.contains(s)){
                    red = 169;
                    green = 169;
                    blue = 169;
                }
                colorSegment(s,red, green, blue, alpha);
            }
        }
        else {

            Random bag = new Random();
            for (Vertex v : vertexList) {
                if (!centroidList.contains(v)){
                    int red = bag.nextInt(255);
                    int green = bag.nextInt(255);
                    int blue = bag.nextInt(255);
                    int alpha = 255;
                    colorVertex(v,red,green,blue, alpha);
                }
                else{
                    colorVertex(v, 0,0,0,0);
                }
            }

            //Coloring the Segments
            for (Segment s : segmentList) {
                if (!neighbourConnectionList.contains(s)){
                    Color v1Color = extractColor(vertexList.get(s.getV1Idx()).getPropertiesList());
                    Color v2Color = extractColor(vertexList.get(s.getV2Idx()).getPropertiesList());
                    int red = (v1Color.getRed() + v2Color.getRed()) / 2;
                    int green = (v1Color.getGreen() + v2Color.getGreen()) / 2;
                    int blue = (v1Color.getBlue() + v2Color.getBlue()) / 2;
                    int alpha = 255;
                    colorSegment(s,red,green,blue,alpha );
                }
                else{
                    colorSegment(s, 0,0,0,0);
                }
            }

        }
        return Mesh.newBuilder().addAllVertices(vertexList).addAllSegments(segmentList).build();

    }

    private void colorVertex(Vertex vertex, int red, int green, int blue, int alpha){
        Random bag = new Random();
        String colorCode = red + "," + green + "," + blue + "," + alpha;
        Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        Vertex colored = Vertex.newBuilder(vertex).addProperties(color).build();
        vertexList.set(vertexList.indexOf(vertex), colored);

    }

    private void colorSegment(Segment seg, int red, int green, int blue, int alpha){
        Property color = Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Segment colored = Segment.newBuilder(seg).addProperties(color).build();
        segmentList.set(segmentList.indexOf(seg), colored);
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
        int alpha = Integer.parseInt(raw[3]);
        return new Color(red, green, blue, alpha);

    }
}


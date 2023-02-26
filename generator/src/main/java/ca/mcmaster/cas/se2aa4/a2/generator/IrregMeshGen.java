package ca.mcmaster.cas.se2aa4.a2.generator;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.generator.MeshGen.*;

import org.locationtech.jts.geom.*;
//import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.awt.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class IrregMeshGen extends GeneralMesh {
    //THESE DATASETS ARE FOR IRREGULAR MESH
    List<Coordinate> coords = new ArrayList<>();
    List<Coordinate[]> allPolygonVertices = new ArrayList<>();
    Set<Vertex> verticesSet = new HashSet<>();
    Set<Segment> segmentsSet = new HashSet<>();

    List<Vertex> siteList = new ArrayList<Vertex>();
    List<Vertex> vertexList = new ArrayList<Vertex>();
    List<Segment> segmentList = new ArrayList<Segment>();
    DecimalFormat precision = new DecimalFormat("0.00");


    //user gets to decide the number of polygons. Will be taken from command line
    public void IrregularMesh() {
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Envelope env = new Envelope(new Coordinate(0, 0), new Coordinate(WIDTH, HEIGHT));
        voronoi.setClipEnvelope(env);//given diagram size constraint
        PrecisionModel pointAcc = new PrecisionModel(0.01);
        GeometryFactory makePolygons = new GeometryFactory(pointAcc);
        voronoi.setTolerance(0.01); //may be redundant

        Random Val = new Random();
        Coordinate Coord = new Coordinate();

        //ARBITRARY VALUE TO BE CHANGED
        int points = 100;

        for (int i = 1; i <= points; i++) {
            int xCoord = Val.nextInt(WIDTH);
            int yCoord = Val.nextInt(HEIGHT);


            System.out.println("Point Coordinates: (" + xCoord + ", " + yCoord + ")");
            Coord.setX(xCoord);
            Coord.setY(yCoord);

            Vertex newSite = Vertex.newBuilder().setX(xCoord).setY(yCoord).build();
            //ADD SITES TO COORDINATE LIST FOR VORONOI BUILDER
            coords.add(Coord);
            //ADD SITES TO VERTEX LIST TO BE ABLE TO VISUALIZE
            siteList.add(newSite);

        }

        voronoi.setSites(coords);

        //allPolygons is like a finished puzzle, it contains all polygons
        Geometry allPolygons = voronoi.getDiagram(makePolygons);
        int numPoly = allPolygons.getNumGeometries(); // num of polygons

        for (int i = 0; i < numPoly; i++) {
            allPolygonVertices.add(allPolygons.getGeometryN(i).getCoordinates()); //will get vertices list of each individual polygon
        }

        for (Coordinate[] vertices : allPolygonVertices) {
            List<Integer> polySegments = new ArrayList<>();
            for (int j = 0; j < vertices.length - 1; j++) {
                Coordinate vert1 = vertices[j];
                Coordinate vert2 = vertices[j + 1];
                double x1 = vert1.getX();
                double y1 = vert1.getY();
                double x2 = vert2.getX();
                double y2 = vert2.getY();
                Vertex v1 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x1))).setY(Double.parseDouble(precision.format(y1))).build();
                Vertex v2 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x2))).setY(Double.parseDouble(precision.format(y2))).build();

                List<Vertex> segments = new ArrayList<Vertex>(Arrays.asList(v1, v2));
                for (Vertex v : segments) {
                    if (!verticesSet.contains(v)) {
                        verticesSet.add(v);
                        vertexList.add(v);
                    }
                }

                Segment s = Segment.newBuilder().setV1Idx(vertexList.indexOf(v1)).setV2Idx(vertexList.indexOf(v2)).build();
                if (!segmentsSet.contains(s)) {
                    segmentsSet.add(s);
                    segmentList.add(s);
                    polySegments.add(segmentList.indexOf(s));
                }
            }
            Polygon poly = Polygon.newBuilder().addAllSegmentIdxs(polySegments).build();
            polygonList.add(poly);

        }

    }

    public Mesh generate() {

        //COLOUR SITE VERTEXES AND SEGMENTS
        for (Vertex v : siteList) {
            int red = 255;
            int green = 0;
            int blue = 0;
            int alpha = 255;
            colorVertex(v, red, green, blue, alpha);

        }

        for (Segment s : segmentList) {
            int red = 0;
            int green = 0;
            int blue = 0;
            int alpha = 255;
            colorSegment(s,red,green,blue,alpha );
            }

        return Mesh.newBuilder().addAllVertices(siteList).addAllSegments(segmentList).build();

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

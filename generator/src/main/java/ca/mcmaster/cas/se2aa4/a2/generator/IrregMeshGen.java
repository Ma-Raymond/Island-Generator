package ca.mcmaster.cas.se2aa4.a2.generator;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;


import org.locationtech.jts.geom.*;
//import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.awt.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class IrregMeshGen extends GeneralMesh {
    //THESE DATASETS ARE FOR IRREGULAR MESH
    List<Vertex> siteList = new ArrayList<Vertex>();

    //user gets to decide the number of polygons. Will be taken from command line
    public void IrregularMesh() {
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Envelope env = new Envelope(new Coordinate(0, 0), new Coordinate(WIDTH, HEIGHT));
        voronoi.setClipEnvelope(env);//given diagram size constraint
        PrecisionModel pointAcc = new PrecisionModel(0.01);
        voronoi.setTolerance(0.01); //may be redundant

        Random Val = new Random();
        Coordinate Coord = new Coordinate();

        //ARBITRARY VALUE TO BE CHANGED
        int points = 100;
        List<Coordinate> coords = new ArrayList<Coordinate>();

        for (int i = 0; i < points; i++) {
            int xCoord = Val.nextInt(WIDTH);
            int yCoord = Val.nextInt(HEIGHT);

            System.out.println("Point Coordinates: (" + xCoord + ", " + yCoord + ")");
            Coord.setX(xCoord);
            Coord.setY(yCoord);

            Vertex newSite = Vertex.newBuilder().setX(xCoord).setY(yCoord).build();
            //ADD SITES TO COORDINATE LIST FOR VORONOI BUILDER
            coords.add(new Coordinate(xCoord,yCoord));
            //ADD SITES TO VERTEX LIST TO BE ABLE TO VISUALIZE
            siteList.add(newSite);
//            vertexList.add(newSite);
        }

        voronoi.setSites(coords);
        GeometryFactory makePolygons = new GeometryFactory();

        //allPolygons is like a finished puzzle, it contains all polygons
        Geometry allPolygons = voronoi.getDiagram(makePolygons);
        Polygonizer polygonizer = new Polygonizer();
        System.out.println("WTFFF"+allPolygons.getNumGeometries());

        int numPoly = allPolygons.getNumGeometries();
        System.out.println(numPoly);

        List<Coordinate[]> allPolygonVertices = new ArrayList<>();
        for (int i = 0; i < numPoly; i++) { //will get vertices list of each individual polygon
            Geometry poly = allPolygons.getGeometryN(i);
            allPolygonVertices.add(poly.getCoordinates());

            // Centroid
            Vertex centroid = Vertex.newBuilder().setX(poly.getCentroid().getX()).setY(poly.getCentroid().getY()).build();
            centroidList.add(centroid);
            vertexList.add(centroid);
        }

        System.out.println("HII" +allPolygonVertices.size());

        for (Coordinate[] vertice : allPolygonVertices) {
            List<Integer> polySegments = new ArrayList<>();

            for (int j = 0; j < vertice.length - 1; j++) {
                Coordinate vert1 = vertice[j];
                Coordinate vert2 = vertice[j + 1];
                double x1 = vert1.getX();
                double y1 = vert1.getY();
                double x2 = vert2.getX();
                double y2 = vert2.getY();
                Vertex v1 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x1))).setY(Double.parseDouble(precision.format(y1))).build();
                Vertex v2 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x2))).setY(Double.parseDouble(precision.format(y2))).build();

                List<Vertex> segment = new ArrayList<Vertex>(Arrays.asList(v1, v2));
                for (Vertex v : segment) {
                    if (!vertices.contains(v)) {
                        vertices.add(v);
                        vertexList.add(v);
                    }
                }

                Segment s = Segment.newBuilder().setV1Idx(vertexList.indexOf(v1)).setV2Idx(vertexList.indexOf(v2)).build();
                if (!segments.contains(s)) {
                    segments.add(s);
                    segmentList.add(s);
                    polySegments.add(segmentList.indexOf(s));
                }
            }
        }
    }

    public Mesh generate() {
        IrregularMesh();
        //COLOUR SITE VERTEXES AND SEGMENTS
        for (Vertex v : vertexList) {
            int red = 255;
            int green = 0;
            int blue = 0;
            int alpha = 255;
            if (!centroidList.contains(v)){
                alpha = 0;
            }
            colorVertex(v, red, green, blue, alpha);

        }

        for (Segment s : segmentList) {
            int red = 0;
            int green = 0;
            int blue = 0;
            int alpha = 255;
            colorSegment(s,red,green,blue,alpha );
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

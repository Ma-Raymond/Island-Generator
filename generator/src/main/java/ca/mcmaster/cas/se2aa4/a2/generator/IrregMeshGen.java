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

// This is Part 3, stilling using the object-oriented approach but with irregular mesh
public class IrregMeshGen extends GeneralMesh {

    public VoronoiDiagramBuilder relaxation(VoronoiDiagramBuilder oldVoronoi){
        centroidList.clear();
        // Relaxed Version
        VoronoiDiagramBuilder relaxVoronoi = new VoronoiDiagramBuilder();

        // Before-Relaxed diagram and polygon
        GeometryFactory makePolygons = new GeometryFactory();
        Geometry polygons = oldVoronoi.getDiagram(makePolygons);
        List<Coordinate> coords = new ArrayList<Coordinate>();  // Coordinate List to set the sites of the new voronoi

        // Goes through each polygon and gets the centroid, and based on that centroid, set them as my new sites for the relaxed voronoi
        for (int i = 0; i < polygons.getNumGeometries(); i++) {
            Geometry poly = polygons.getGeometryN(i);
            double xCoord = poly.getCentroid().getX();
            double yCoord = poly.getCentroid().getY();
            coords.add(new Coordinate(xCoord,yCoord));

            // This to consistently keep centroidList updated everytime user wants to relax the diagram
            centroidList.add(Vertex.newBuilder().setX(xCoord).setY(yCoord).build());
        }
        // Set new coords to the new diagram
        relaxVoronoi.setSites(coords);
        return relaxVoronoi;
    }
    //user gets to decide the number of polygons. Will be taken from command line


    public void IrregularMesh(int numberPolygons) {
        VoronoiDiagramBuilder voronoi = new VoronoiDiagramBuilder();
        Envelope env = new Envelope(new Coordinate(0, 0), new Coordinate(WIDTH, HEIGHT));
        voronoi.setClipEnvelope(env);//given diagram size constraint
        PrecisionModel pointAcc = new PrecisionModel(0.01);
        voronoi.setTolerance(0.01); //may be redundant
        Random Val = new Random();


        //Takes user input for the number of sites
        int points = numberPolygons;
        List<Coordinate> coords = new ArrayList<Coordinate>();

        // Randomly generates coordinates based on the canvas size
        for (int i = 0; i < points; i++) {
            int xCoord = Val.nextInt(WIDTH);
            int yCoord = Val.nextInt(HEIGHT);

            System.out.println("Point Coordinates: (" + xCoord + ", " + yCoord + ")");

            //ADD SITES TO COORDINATE LIST FOR VORONOI BUILDER
            coords.add(new Coordinate(xCoord,yCoord));
        }
        // SET THE COORDINATES AS SITES TO THE VORONOI
        voronoi.setSites(coords);
        GeometryFactory makePolygons = new GeometryFactory();

        // RELAXATION -----------------------
        int relaxAmount = 3;        // RELAXATION AMOUNT BASED ON USER INPUT
        for (int i=0; i < relaxAmount; i++){    // Based on user input, how many times to relax
            voronoi = relaxation(voronoi);      // Sets the voronoi to a new relaxed voronoi
        }
        // Adds all the centroids vertexes after relaxation, to the vertexList
        vertexList.addAll(centroidList);

        //allPolygons is like a finished puzzle, it contains all polygons
        Geometry allPolygons = voronoi.getDiagram(makePolygons);

        int numPoly = allPolygons.getNumGeometries();
        // This variable will hold the Coordinates of all the polygon shape
        List<Coordinate[]> allPolygonVertices = new ArrayList<>();
        for (int i = 0; i < numPoly; i++) { //will get vertices list of each individual polygon
            Geometry poly = allPolygons.getGeometryN(i); // Indivdiual Polygon N
            allPolygonVertices.add(poly.getCoordinates());  // Add the coordinates of the polygon N into the polygonVertices list
        }

        // For each coordinate, which is apart of a polygon
        for (Coordinate[] vertice : allPolygonVertices) {
            // Create Segments between each point connecting all the points in the polygon making a shape
            for (int j = 0; j < vertice.length - 1; j++) {
                // Get the two Vertices
                Coordinate vert1 = vertice[j];
                Coordinate vert2 = vertice[j + 1];
                // Get all the coordinates of V1 and V2
                double x1 = vert1.getX();
                double y1 = vert1.getY();
                double x2 = vert2.getX();
                double y2 = vert2.getY();
                // Make two Vertex with those coordinates
                Vertex v1 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x1))).setY(Double.parseDouble(precision.format(y1))).build();
                Vertex v2 = Vertex.newBuilder().setX(Double.parseDouble(precision.format(x2))).setY(Double.parseDouble(precision.format(y2))).build();

                // Check if vertex already exist using a SET to double check the vertices
                List<Vertex> segment = new ArrayList<Vertex>(Arrays.asList(v1, v2));
                for (Vertex v : segment) {
                    if (!vertices.contains(v)) {        // IF VERTEX NOT IN THE SET ALREADY, ADD IT
                        vertices.add(v);                // ADD IT TO THE SET
                        vertexList.add(v);              // ADD IT TO THE VERTEX LIST
                    }
                }
                // After checking if ther vertex already exist, we can make the segment and check if the segment already exist or not.
                Segment s = Segment.newBuilder().setV1Idx(vertexList.indexOf(v1)).setV2Idx(vertexList.indexOf(v2)).build();
                if (!segments.contains(s)) {        // IF SEGMENT NOT IN THE SET ALREADY, ADD IT
                    segments.add(s);                // ADD IT TO THE SET
                    segmentList.add(s);             // ADD IT TO THE SEGMENT LIST
                }
            }
        }
    }

    /**
     * Runs the Irregular Mesh function, creates colors for segments and vertices
     * @return Mesh including all the colored vertexes and segments
     */

    public Mesh generate(int numberPolygons) {
        IrregularMesh(numberPolygons);
        //COLOUR SITE VERTEXES AND SEGMENTS
        for (Vertex v : vertexList) {
            int red = 255;
            int green = 0;
            int blue = 0;
            int alpha = 255;
            if (!centroidList.contains(v)){     // IF NOT A CENTROID, MAKE IT INVISIBLE
                alpha = 0;
            }
            colorVertex(v, red, green, blue, alpha);
        }
        // For each segment in the segmentlist apply a color to it
        for (Segment s : segmentList) {
            int red = 0;
            int green = 0;
            int blue = 0;
            int alpha = 255;
            colorSegment(s,red,green,blue,alpha );      // COLOUR THE SEGMENTS BLACK
            }

        return Mesh.newBuilder().addAllVertices(vertexList).addAllSegments(segmentList).build();

    }

    private void colorVertex(Vertex vertex, int red, int green, int blue, int alpha){
        Random bag = new Random();
        String colorCode = red + "," + green + "," + blue + "," + alpha;
        // Create new Property with "rgb_color" key and the rgb value as the value
        Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        Vertex colored = Vertex.newBuilder(vertex).addProperties(color).build();
        // Set the old vertex in the list as the new one with color property
        vertexList.set(vertexList.indexOf(vertex), colored);
    }

    private void colorSegment(Segment seg, int red, int green, int blue, int alpha){
        // Create new Property with "rgb_color" key and the rgb value as the value
        Property color = Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Segment colored = Segment.newBuilder(seg).addProperties(color).build();
        // Set the old segment in the list as the new one with color property
        segmentList.set(segmentList.indexOf(seg), colored);
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        // EXTRACTCOLOR GOES THROUGH ALL THE PROPERTIES OF THE OBJECT
        for(Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null) {       // IF THE RGB COLOR PROPERTY DOESN'T EXIST, COVER THAT CASE BY MAKING IT BLACK
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

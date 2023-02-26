package ca.mcmaster.cas.se2aa4.a2.generator;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.*;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

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

public class IrregMeshGen extends GeneralMesh {
    //THESE DATASETS ARE FOR IRREGULAR MESH
    List<Coordinate> coords = new ArrayList<>();
    List<Polygon> voronoiPolygons = new ArrayList<>();
    DecimalFormat precision  = new DecimalFormat("0.00");


    //user gets to decide the number of polygons. Will be taken from command line
    public void irregularMesh(int numPoly) {
        GeometryFactory voronoi = new GeometryFactory();
        Random xVal = new Random();
        Random yVal = new Random();
        //GIVE ARBITRARY NUMBER OF SITES FOR NOW numPoly WILL BE USED LATER
        int points = 100;

        for (int i = 1; i <= points; i++) {
            int xCoord = xVal.nextInt(width);
            int yCoord = yVal.nextInt(height);

            System.out.println("Point Coordinates: (" + xCoord + ", " + yCoord + ")");
            Coordinate newPoint = new Coordinate(xCoord, yCoord);
            Vertex newSite = Vertex.newBuilder().setX(xCoord).setY(yCoord).build();
            //ADD SITES TO COORDINATE LIST FOR VORONOI BUILDER
            coords.add(newPoint);
            //ADD SITES TO VERTEX LIST TO BE ABLE TO VISUALIZE
            vertexList.add(newSite);

            VoronoiDiagramBuilder newDiagram = new VoronoiDiagramBuilder();
            newDiagram.setSites(coords);

            Geometry makePolygons = newDiagram.getDiagram(voronoi);

            //FIGURE OUT HOW TO GET VERTICES GENERATED BY VORONOI DIAGRAM

        }
    }
}